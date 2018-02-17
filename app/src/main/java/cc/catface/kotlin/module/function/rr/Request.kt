package cc.catface.kotlin.module.function.rr

import cc.catface.clibrary.util.extension.d
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
object Request {

    fun login() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        val api = retrofit.create(RetrofitApi::class.java)

        d("开始请求服务器")
        api.login("catface王夜寒","root")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext({ t -> println("---") })

//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Subscriber<String>() {
//                    override fun onNext(t: String?) {
//
//                    }
//
//                    override fun onError(e: Throwable?) {
//                    }
//
//                    override fun onCompleted() {
//                    }
//
//                })
    }


    /*fun t() {

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .baseUrl("")
                .build()
        val service = retrofit.create(RetrofitApi::class.java!!)

        service.login(mapOf(), mapOf())               //获取Observable对象
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .doOnNext(Action1<Any> { userInfo ->
                    //                    saveUserInfo(userInfo)//保存用户信息到本地
                })
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(object : Subscriber<String>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        //请求失败
                    }

                    override fun onNext(userInfo: String) {
                        //请求成功
                    }
                })
    }*/

}