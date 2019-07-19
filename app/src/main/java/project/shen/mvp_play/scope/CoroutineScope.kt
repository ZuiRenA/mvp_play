package project.shen.mvp_play.scope

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext


interface MainScoped {
    companion object {
        internal val scopeMap = IdentityHashMap<MainScoped, MainScope>()
    }

    val mainScope: MainScope
        get() = scopeMap[this]!!

    fun createScope() {
        val activity by lazy {
            this
        }

        scopeMap[activity] ?: MainScope().also {
            scopeMap[activity] = it as MainScope
        }
    }

    fun destroyScope() {
        scopeMap.remove(this)?.cancel()
    }

    fun <T> withScope(block: CoroutineScope.() -> T) = with(mainScope, block)

    fun View.onClickSuspend(handler: suspend CoroutineScope.(v: View) -> Unit) {
        setOnClickListener {
            mainScope.launch {
                handler(it)
            }
        }
    }
}

fun View.onClickAutoDisposable(
    context: CoroutineContext = Dispatchers.Main,
    handler: suspend CoroutineScope.(v: View) -> Unit
) {
    setOnClickListener {
        GlobalScope.launch {
            handler(it)
        }.asAutoDisposable(it)
    }
}

fun Job.asAutoDisposable(view: View) = AutoDisposable(view, this)

class ActivityLifecycleCallbackImpl: BaseActivityLifecycleCallbacks() {
    override fun onActivityDestroyed(activity: Activity?) {
        (activity as? MainScoped)?.createScope()
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        (activity as? MainScoped)?.destroyScope()
    }
}

abstract class BaseActivityLifecycleCallbacks: Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity?) {}
    override fun onActivityResumed(activity: Activity?) {}

    override fun onActivityStarted(activity: Activity?) {}

    abstract override fun onActivityDestroyed(activity: Activity?)

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityStopped(activity: Activity?) {}

    abstract override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?)
}

interface MainScope: CoroutineScope{

    companion object {
        internal var isSetUp = false
        internal var isDebug = false

        val isFragmentSupported by lazy {
            try {
                Class.forName("android.support.v4.app.FragmentManager\$FragmentLifecycleCallbacks")
                Logcat.debug("Fragment enabled.")
                true
            } catch (e: ClassNotFoundException) {
                Logcat.debug("Fragment disabled.")
                Logcat.error(e)
                false
            }
        }
    }

    fun setUp(application: Application): Companion {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbackImpl())
        isSetUp = true
        return Companion
    }

    fun enableDebug() {
        isDebug = true
    }
}

object Logcat {
    private const val TAG = "MainScope"

    fun debug(log: Any?) = MainScope.isDebug.whenTrue{ Log.d(TAG, log.toString()) }

    fun warn(log: Any?) = MainScope.isDebug.whenTrue{ Log.w(TAG, log.toString()) }

    fun error(log: Any?) = MainScope.isDebug.whenTrue{ Log.e(TAG, log.toString()) }

    private fun Boolean.whenTrue(block: () -> Unit){
        if(this){
            block()
        }
    }
}

class UnsupportedTypeException(type: Class<*>, vararg supportedTypes: String)
    : Exception("Unsupported type: $type. ${supportedTypes.joinToString()} ${if(supportedTypes.size == 1) "is" else "are" } needed.")

class UnsupportedVersionException(library: String, version: String): Exception("Unsupported version: $version of $library")

