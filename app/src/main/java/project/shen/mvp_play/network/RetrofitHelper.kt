package project.shen.mvp_play.network

import android.content.ContentValues.TAG
import android.util.Log
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import project.shen.mvp_play.network.NetConfig.BASE_URL
import project.shen.mvp_play.network.NetConfig.FORMAT_KEY
import project.shen.mvp_play.network.NetConfig.FORMAT_VALUE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.lang.StringBuilder
import java.text.Format
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    private const val TIME_OUT = 10L

    private fun getRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                try {
                    val original = chain.request()
                    val request = original.newBuilder()
                        .url(original.url().newBuilder().build())
                        .header(FORMAT_KEY, FORMAT_VALUE)
                        .method(original.method(), original.body())
                        .build()

                    if (request.method() == "POST") {
                        val sb = StringBuilder()
                        if (request.body() is FormBody) {
                            val body = request as FormBody?
                            for (i in 0 until (body?.size() ?: 0)) {
                                sb.append(body?.encodedName(i) + "=" + body?.encodedValue(i))
                            }
                            sb.delete(sb.length -1, sb.length)
                            Log.d(TAG, "| body:{$sb}")
                            Log.d(TAG, request.body().toString())
                        }
                    }

                    return@addInterceptor chain.proceed(request)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                null
            }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(loggingInterceptor)
        val client = httpClient.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun <T> create(clazz: Class<T>): T = getRetrofit().create(clazz)
}