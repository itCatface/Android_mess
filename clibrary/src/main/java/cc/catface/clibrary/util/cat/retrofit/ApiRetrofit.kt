package cc.catface.clibrary.util.cat.retrofit

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by Administrator on 2017/11/23/023.
 */
interface ApiRetrofit {


    @POST("/")
    fun login(@Body request: LoginRequest): Observable<LoginResponse>

    @GET("/")
    fun register(@Body request: RegisterRequest): Observable<RegisterResponse>


}


