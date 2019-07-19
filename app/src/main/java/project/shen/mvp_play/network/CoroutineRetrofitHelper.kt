package project.shen.mvp_play.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.suspendCancellableCoroutine
import project.shen.mvp_play.model.User
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.Exception
import java.lang.NullPointerException
import kotlin.coroutines.resumeWithException

object CoroutineRetrofitHelper {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(clazz: Class<T>): T = retrofit.create(clazz)
}

interface CallBack<T> {
    fun onSuccess(result: T)
    fun onError(t: Throwable)
}

/**
 * 协程 X Retrofit的网络请求取消
 */
public suspend fun <T: Any> Call<T>.await(): T {
    return suspendCancellableCoroutine { coroutinuation ->
        enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                if (coroutinuation.isCancelled) return
                coroutinuation.resumeWithException(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                coroutinuation.resumeWith(kotlin.runCatching {
                    if (response.isSuccessful) {
                        response.body()
                            ?: throw NullPointerException("Response body is null: $response")
                    } else {
                        throw HttpException(response)
                    }
                })
            }
        })

        coroutinuation.invokeOnCancellation {
            try {
                cancel()
            } catch (ex: Throwable) {
                //Ignore cancel exception
                //如果不进行异常捕获，抛出的异常相当于协程体抛出，可以在外部catch
            }
        }
    }
}

interface GitHubApi {
    @GET("users/{login}")
    suspend fun getUserAsync(@Path("login") login: String): Deferred<User>
}



