package cc.catface.kotlin.module.function.rr

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
interface RetrofitApi {

    @GET("hi/login")
    fun login(@QueryMap map: Map<String, String>): Observable<String>


    @GET("hi/login")
    fun login(@Query("username") username: String, @Query("password") password: String): Observable<String>

}