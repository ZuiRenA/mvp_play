package project.shen.mvp_play.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import project.shen.mvp_play.R
import project.shen.mvp_play.imvp.CommonModule
import project.shen.mvp_play.imvp.DaggerCommonComponent
import project.shen.mvp_play.imvp.ICommonView
import project.shen.mvp_play.model.User
import project.shen.mvp_play.network.CallBack
import project.shen.mvp_play.presenter.LoginPresenter
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoginActivity : AppCompatActivity(), ICommonView {

    @Inject
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        DaggerCommonComponent.builder()
            .commonModule(CommonModule(this))
            .build()
            .inject(this)

        btnLogin.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main + defaultCoroutineExceptionHandler) {
                try {
                    presenter.login(getUserCoroutine())
                } catch (e: Exception) {

                }
            }
        }
    }

    override fun getContext(): Context = this
}

suspend fun getUserCoroutine() = suspendCoroutine<User> { continuation ->
    getUser(object : CallBack<User> {
        override fun onSuccess(result: User) {
            continuation.resume(result)
        }

        override fun onError(t: Throwable) {
            continuation.resumeWithException(t)
        }
    })
}

fun <T> getUser(callBack: CallBack<T>) {

}

val defaultCoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

}