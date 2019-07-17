package project.shen.mvp_play.presenter

import android.util.Log
import project.shen.mvp_play.imvp.ICommonView
import javax.inject.Inject

class MainPresenter @Inject constructor(private var iCommonView: ICommonView) {

    fun wow() {
        val context = iCommonView.getContext()
        Log.d("MainPresenter", "$context")
    }
}