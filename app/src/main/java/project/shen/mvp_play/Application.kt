package project.shen.mvp_play

import android.app.Application
import project.shen.mvp_play.scope.ActivityLifecycleCallbackImpl
import project.shen.mvp_play.scope.MainScoped

class MyApplication : Application() {

    companion object {
        private lateinit var app: MyApplication

        fun getApplication() = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbackImpl())
    }

}