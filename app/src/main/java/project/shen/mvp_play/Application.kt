package project.shen.mvp_play

import android.app.Application

class MyApplication : Application() {

    companion object {
        private lateinit var app: MyApplication

        fun getApplication() = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }

}