package project.shen.mvp_play.scope

import android.os.Build
import android.view.View
import kotlinx.coroutines.Job

class AutoDisposable(private val view: View,private val job: Job): Job by job, View.OnAttachStateChangeListener{
    override fun onViewDetachedFromWindow(v: View?) {
        cancel()
        view.removeOnAttachStateChangeListener(this)
    }

    override fun onViewAttachedToWindow(v: View?) = Unit

    private fun isViewAttached() =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && view.isAttachedToWindow || view.windowToken != null

    init {
        if (isViewAttached())
            view.addOnAttachStateChangeListener(this)
        else
            cancel()

        invokeOnCompletion {
            view.post {
                view.removeOnAttachStateChangeListener(this)
            }
        }
    }
}
