package project.shen.mvp_play.view

import android.app.Activity
import android.os.Bundle
import kotlinx.coroutines.*

abstract class ScopeActivity : Activity(), CoroutineScope by MainScope() {
    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}

class CoroutineActivity: ScopeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launch {

        }
    }
}