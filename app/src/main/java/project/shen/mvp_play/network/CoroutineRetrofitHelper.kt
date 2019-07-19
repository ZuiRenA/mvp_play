package project.shen.mvp_play.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import project.shen.mvp_play.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object CoroutineRetrofitHelper {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(clazz: Class<T>): T = retrofit.create(clazz)
}

interface CallBack<T> {
    fun onSuccess(result: T)
    fun onError(t: Throwable)
}

interface GitHubApi {

    @GET("users/{login}")
    suspend fun getUser(@Path("login") login: String): User
}

