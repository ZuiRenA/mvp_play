package project.shen.mvp_play.presenter

import android.util.Log
import android.widget.Toast
import project.shen.mvp_play.MyApplication
import project.shen.mvp_play.imvp.ICommonView
import project.shen.mvp_play.model.User
import javax.inject.Inject

class LoginPresenter @Inject constructor (private var iView: ICommonView) {

    fun login(user: User) {
        val context = iView.getContext()
        Log.d("LoginPresenter", "$context")
        Toast.makeText(MyApplication.getApplication(),
            "id = ${user.id}, userName = ${user.userName}" +
                ", pwd = ${user.pwd}", Toast.LENGTH_LONG).show()
    }
}