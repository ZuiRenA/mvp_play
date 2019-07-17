package project.shen.mvp_play.imvp

import android.app.Activity
import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import project.shen.mvp_play.view.LoginActivity
import project.shen.mvp_play.view.MainActivity
import javax.inject.Scope
import javax.inject.Singleton

interface ICommonView {
    fun getContext(): Context
}

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Module
class CommonModule(private var iCommonView: ICommonView) {

    @Provides
    @ActivityScope
    fun provideICommonView(): ICommonView = iCommonView
}

@Singleton
@ActivityScope
@Component(modules = [CommonModule::class])
interface CommonComponent {
    fun inject(activity: LoginActivity)
    fun inject(activity: MainActivity)
}