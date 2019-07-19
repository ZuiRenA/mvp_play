package project.shen.mvp_play.network

import android.net.ParseException
import com.google.gson.JsonParseException
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import project.shen.mvp_play.model.Response
import project.shen.mvp_play.model.User
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

fun <T: Any> KClass<T>.retrofitCreate() = RetrofitHelper.create(this.java)

fun <T> Class<T>.retrofitCreate() = RetrofitHelper.create(this)

fun <T> Observable<Response<T>>.resultHandler(
    resultHandle:(T) -> Unit,
    errorHandle:(Throwable) -> Unit
): Disposable = this.compose(RXNetWorkUtil.handleResult())
    .compose(SchedulerProvider.applySchedulers())
    .subscribe({
        resultHandle(it)
    }) {
        errorHandle(it)
    }


object RXNetWorkUtil {
    fun <T> handleResult(): ObservableTransformer<Response<T>, T> =
        ObservableTransformer {
            it.onErrorResumeNext(ErrorResumeFunction())
                .flatMap(ResponseFunction())
        }

    class ErrorResumeFunction<T>: Function<Throwable, ObservableSource<out Response<T>>> {
        override fun apply(t: Throwable): ObservableSource<out Response<T>> {
            return Observable.error(CustomException.handleException(t))
        }
    }

    class ResponseFunction<T>: Function<Response<T>, ObservableSource<T>> {
        override fun apply(t: Response<T>): ObservableSource<T> {
            val code = t.code
            val msg = t.msg
            return if (code == 200) {
                Observable.just(t.result)
            } else {
                Observable.error(ApiException(code, msg))
            }
        }
    }
}

object SchedulerProvider: BaseSchedulerProvider() {
    override fun computation(): Scheduler = Schedulers.computation()

    override fun io(): Scheduler = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun <T> applySchedulers(): ObservableTransformer<T, T> =
        ObservableTransformer { observable ->
            observable.subscribeOn(io())
                .observeOn(ui())
        }
}

class ApiException: Exception {
    constructor(code: Int, displayMessage: String?) {
        this.code = code
        this.displayMessage = displayMessage
    }
    constructor(code: Int, message: String, displayMessage: String?): super(message) {
        this.code = code
        this.displayMessage = displayMessage
        this.message = message
    }

    private var code = 0
    private var displayMessage: String?
    override lateinit var message: String

    fun getCode(): Int = code

    fun setCode(code: Int) {
        this.code = code
    }

    fun getDisplayMessage() = displayMessage

    fun setDisplayMessage(displayMessage: String) {
        this.displayMessage = displayMessage
    }
}

object CustomException {
    /**
     * 未知错误
     */
    private const val UNKNOWN = 1000

    /**
     * 解析错误
     */
    private const val PARSE_ERROR = 1001

    /**
     * 网络错误
     */
    private const val NETWORK_ERROR = 1002

    /**
     * 协议错误
     */
    private const val HTTP_ERROR = 1003

    fun handleException(e: Throwable): ApiException {
        val ex: ApiException
        if (e is JsonParseException
            || e is JSONException
            || e is ParseException
        ) {
            //解析错误
            ex = ApiException(PARSE_ERROR, e.message)
            return ex
        } else if (e is ConnectException) {
            //网络错误
            ex = ApiException(NETWORK_ERROR, e.message)
            return ex
        } else if (e is UnknownHostException || e is SocketTimeoutException) {
            //连接错误
            ex = ApiException(NETWORK_ERROR, e.message)
            return ex
        } else {
            //未知错误
            ex = ApiException(UNKNOWN, e.message)
            return ex
        }
    }
}

abstract class BaseSchedulerProvider {
    abstract fun computation(): Scheduler

    abstract fun io(): Scheduler

    abstract fun ui(): Scheduler

    abstract fun <T> applySchedulers(): ObservableTransformer<T, T>
}