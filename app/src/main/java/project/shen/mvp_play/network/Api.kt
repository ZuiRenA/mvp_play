package project.shen.mvp_play.network

import io.reactivex.Observable
import project.shen.mvp_play.model.Response
import project.shen.mvp_play.model.User
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @POST("")
    fun getList(@Query("id") id: String): Observable<Response<User>>

}