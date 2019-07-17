package project.shen.mvp_play.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import project.shen.mvp_play.R
import project.shen.mvp_play.imvp.CommonModule
import project.shen.mvp_play.imvp.DaggerCommonComponent
import project.shen.mvp_play.imvp.ICommonView
import project.shen.mvp_play.model.User
import project.shen.mvp_play.presenter.LoginPresenter
import javax.inject.Inject

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
            presenter.login(User(
                id = 1,
                userName = "userName",
                pwd = "pwd"
            ))
        }
    }

    override fun getContext(): Context = this
}
