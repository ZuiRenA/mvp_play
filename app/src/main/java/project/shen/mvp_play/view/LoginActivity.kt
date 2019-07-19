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
import project.shen.mvp_play.network.CoroutineRetrofitHelper
import project.shen.mvp_play.network.GitHubApi
import project.shen.mvp_play.presenter.LoginPresenter
import project.shen.mvp_play.scope.MainScoped
import project.shen.mvp_play.scope.onClickAutoDisposable
import java.lang.Exception
import javax.inject.Inject

class LoginActivity : ScopeActivity(), ICommonView, MainScoped {

    @Inject
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        DaggerCommonComponent.builder()
            .commonModule(CommonModule(this))
            .build()
            .inject(this)

//        btnLogin.setOnClickListener {
//            GlobalScope.launch(Dispatchers.Main) {
//                val deferred = CoroutineRetrofitHelper.create(GitHubApi::class.java)
//                    .getUserAsync("")
//
//                withContext(Dispatchers.IO) {
//                    deferred.cancel()
//                }
//                try {
//                    presenter.login(deferred.await())
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }

        btnLogin.onClickSuspend {
            withScope {

            }
        }

        btnLogin.onClickAutoDisposable {

        }
    }

    override fun getContext(): Context = this
}
