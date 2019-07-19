package project.shen.mvp_play.presenter

import android.util.Log
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import project.shen.mvp_play.MyApplication
import project.shen.mvp_play.imvp.ICommonView
import project.shen.mvp_play.model.Response
import project.shen.mvp_play.model.User
import project.shen.mvp_play.network.RXNetWorkUtil
import project.shen.mvp_play.network.SchedulerProvider
import javax.inject.Inject

class LoginPresenter @Inject constructor (private var iView: ICommonView) {


    fun login(user: User) {
        val context = iView.getContext()
    }

    fun login(response: Response<List<User>>) {

    }


}