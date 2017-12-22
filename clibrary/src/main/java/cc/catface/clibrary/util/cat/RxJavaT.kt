package cc.catface.clibrary.util.cat

import android.content.Context
import android.os.SystemClock
import cc.catface.clibrary.util.LogT
import cc.catface.clibrary.util.cat.retrofit.ApiRetrofit
import cc.catface.clibrary.util.cat.retrofit.LoginRequest
import cc.catface.clibrary.util.cat.retrofit.LoginResponse
import cc.catface.clibrary.util.cat.retrofit.UserClient
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Created by wyh
 */
object RxJavaT {


    /**
     * observable与observer
     */
    fun observerDemo() {
        val observable = Observable.create<Int> { emitter: ObservableEmitter<Int> ->

            for (i in 1..5) {
                LogT.d("第 $i 个数已发送")
                emitter.onNext(i)
            }
        }

        val observer = object : Observer<Int> {

            var disposable: Disposable? = null

            override fun onSubscribe(d: Disposable) {
                LogT.d("观察者onSubscribe")
                disposable = d
            }

            override fun onNext(t: Int) {
                if (t == 3) {
                    disposable?.dispose()
                    LogT.d("$t 已被拦截")
                }

                LogT.d("观察者onNext接受到$t")
            }

            override fun onComplete() {
                LogT.d("观察者onComplete")
            }

            override fun onError(e: Throwable) {
                LogT.d("观察者onError")
            }
        }

        observable.subscribe(observer)
    }
    /*1发送->接收1，2发送->接收2...[同一个个线程中]*/


    /**
     * observable与consumer
     */
    fun consumerDemo() {
        val observable = Observable.create<Int> { emitter: ObservableEmitter<Int> ->
            for (i in 1..5) {
                LogT.d("发射第$i")
                emitter.onNext(i)
            }
        }


        val consumer = Consumer<Int> { i -> LogT.d("Consumer accept到了$i") }

        observable.subscribe(consumer)
    }


    /**
     * Observable和Observer的线程
     */
    fun threadDemo() {
        val observable = Observable.create<Int> { emitter: ObservableEmitter<Int> ->
            LogT.d("被观察者线程: " + Thread.currentThread().name)
            emitter.onNext(0)
        }

        val consumer = Consumer<Int> { t ->
            LogT.d("观察者线程: " + Thread.currentThread().name + " || 接受到$t")
        }

        observable.subscribe(consumer)

        // 多次添加注册线程取第一次
        // 多次添加观察线程取最后一次
        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(Consumer { t -> LogT.d("doOnNext's start thread is: " + Thread.currentThread().name) })
                .observeOn(Schedulers.newThread())
                .doOnNext(Consumer { t -> LogT.d("doOnNext's end thread is: " + Thread.currentThread().name) })
                .subscribe(consumer)

        /* * RxJava提供的各个内置线程

         * 1. Schedulers.io() : 网络、IO etc.
         * 2. Schedulers.computation() : calc
         * 3. Schedulers.newThread() : normal new thread
         * 4. AndroidSchedulers.mainThread() : ui
         * */
    }


    /**
     * 在子线程中请求网络后在主线程中更新UI
     */
    fun threadUI(ctx: Context) {
        val observable = Observable.create<LoginResponse> { emitter: ObservableEmitter<LoginResponse> ->
            val text = URL("http://www.baidu.com").readText()
            LogT.d(text)
        }

        val observer = object : Observer<LoginResponse> {
            override fun onError(e: Throwable) {
                LogT.t(ctx, "error" + e.toString())
            }

            override fun onComplete() {
                LogT.t(ctx, "success")
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(response: LoginResponse) {

            }
        }

        val api = UserClient.get().create(ApiRetrofit::class.java)
        api.login(LoginRequest()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }


    /**
     * map操作
     */
    fun mapDemo() {
        Observable.create<Int> { emitter: ObservableEmitter<Int> ->
            for (i in 1..5) {
                emitter.onNext(i)
            }
        }.map { i -> "This is result " + i; }.subscribe(Consumer { s -> LogT.d(s) })
    }


    /**
     * flatMap操作[分割事件后无序发送]
     */
    fun flatMapDemo() {
        Observable.create<Int> { emitter: ObservableEmitter<Int> ->
            for (i in 1..3) {
                emitter.onNext(i)
            }
        }.flatMap { i ->
            val list = arrayListOf("我是$i 一次", "我是$i 二次", "我是$i 三次")
            Observable.fromIterable(list).delay(5, TimeUnit.SECONDS)
        }.subscribe(Consumer { s ->
            LogT.d("接受到$s")
        })
    }


    /**
     * concatMap操作[分割事件后有序发送]
     */
    fun concatMapDemo() {
        Observable.create<Int> { e: ObservableEmitter<Int> ->
            for (i in 1..3) {
                e.onNext(i)
            }
        }.concatMap { i ->
            val list = arrayListOf("我是$i 一次\", \"我是$i 二次\", \"我是$i 三次")
            Observable.fromIterable(list).delay(5, TimeUnit.SECONDS)
        }.subscribe(Consumer { s ->
            LogT.d(s)
        })
    }


    /**
     * 应用场景：展示用户的信息需要从两个服务器接口中获取时
     * 当两个接口都返回信息才进行展示
     */
    fun zipDemo1() {
        val observable1 = Observable.create<Int> { e: ObservableEmitter<Int> ->
            for (i in 1..5) {
                LogT.d("观察者1发送$i")
                SystemClock.sleep(100)
                e.onNext(i)
            }
            e.onComplete()
        }.subscribeOn(Schedulers.io())  // 如果多个Observable不在新开的子线程中跑那就是顺序执行发送完每个Observable的内容


        val observable2 = Observable.create<String> { e: ObservableEmitter<String> ->
            for (i in 1..4) {
                LogT.d("观察者2发送$i")
                SystemClock.sleep(100)
                e.onNext("--*v$i*")
            }
            e.onComplete()
        }.subscribeOn(Schedulers.io())

        /* 将多个Observerbal发送的事件绑定到一起组合成一个新的Observable再发送[木桶原理] */
        Observable.zip(observable1, observable2, BiFunction<Int, String, String> { i, s ->
            "" + i + s
        }).subscribe(object : Observer<String> {
            override fun onNext(t: String) {
                LogT.d(t)
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
            }

            override fun onSubscribe(d: Disposable) {
            }

        })
    }


    /**
     * Back pressure
     */
    fun backPressureDemo() {
        val observable1 = Observable.create<Int> { e: ObservableEmitter<Int> ->
            var i = 0
            while (true) {
                e.onNext(++i)
                LogT.d("观察者1发送$i")
            }

        }.subscribeOn(Schedulers.io())

        val observable2 = Observable.create<Int> { e: ObservableEmitter<Int> ->
            var i = 0
            while (true) {
                e.onNext(++i)
                LogT.d("观察者2发送$i")
            }

        }.subscribeOn(Schedulers.io())

        val observable3 = Observable.create<String> { e: ObservableEmitter<String> ->
            e.onNext("AAA")
        }.subscribeOn(Schedulers.io())

        Observable.zip(observable1, observable2, observable3, Function3<Int, Int, String, String> { i1, i2, s ->
            "$i1 -- $i2 -- $s"
        }).observeOn(AndroidSchedulers.mainThread()).subscribe({ s ->
            LogT.d(s)
        }, { throwable ->
            LogT.d("2,,$throwable")
        })
    }


    fun filterDemo() {
        Observable.create<Int> { e: ObservableEmitter<Int> ->
            var i = 0
            while (true) {
                e.onNext(++i)
                LogT.d("发送了$i")
            }
        }.subscribeOn(Schedulers.io()).filter { t ->
            t % 100 == 0
        }.observeOn(AndroidSchedulers.mainThread()).subscribe(Consumer { i ->
            LogT.d("接收到了$i")
        })
    }


    /**
     * 相隔指定时间后发送事件，将该事件放入池内，但是会丢失许多事件(减少池内事件)
     */
    fun sampleDemo() {
        Observable.create<Int> { e: ObservableEmitter<Int> ->
            var i = 0
            while (true) {
                e.onNext(++i)
            }
        }.subscribeOn(Schedulers.io())
                .sample(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { t ->
                    LogT.d("接收到$t")
                })
    }

    /**
     * 控制Observable发送速度
     */
    fun speedDemo() {
        Observable.create<Int> { e: ObservableEmitter<Int> ->
            var i = 0
            while (true) {
                e.onNext(++i)
                SystemClock.sleep(2000)
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { t ->
                    LogT.d("接受到$t")
                })
    }


    /**
     * Flowable默认有128大小的池
     */
    fun flowableDemo1() {
        val flowable = Flowable.create(FlowableOnSubscribe<Int> { e ->
            for (i in 1..5) {
                LogT.d("发送了$i")
                e.onNext(i) // 这边发送那边就接收到了
            }

            LogT.d("发送完成")
            e.onComplete()
        }, BackpressureStrategy.ERROR)  // MissingBackpressureException


        val subscriber = object : Subscriber<Int> {
            override fun onNext(t: Int?) {
                LogT.d("接受到$t")
            }

            override fun onError(t: Throwable?) {
                LogT.d("onError" + t.toString())
            }

            override fun onSubscribe(s: Subscription) {
                LogT.d("onSubscribe$s")

                // 观察者处理事件的能力
                s.request(Long.MAX_VALUE)

                // 4发送过来处理不了了(超过处理范围) -> MissingBackpressureException: create: could not emit value due to lack of requests
//                s.request(3)

                // s.cancel() 功能(切断事件流管道)同 d.dispose()
            }

            override fun onComplete() {
                LogT.d("接收完成")
            }
        }

        flowable.subscribe(subscriber)
    }


    /**
     * 自己控制接收事件
     */
    fun flowableDemo2() {
        val flowable = Flowable.create(FlowableOnSubscribe<Int> { e ->
            for (i in 1..5) {
                LogT.d("发送了$i")
                e.onNext(i) // 这边发送那边就接收到了
            }

            LogT.d("发送完成")
            e.onComplete()
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())  // MissingBackpressureException


        val subscriber = object : Subscriber<Int> {
            override fun onNext(t: Int?) {
                LogT.d("接受到$t")
            }

            override fun onError(t: Throwable?) {
                LogT.d("onError" + t.toString())
            }

            override fun onSubscribe(s: Subscription) {
                LogT.d("onSubscribe$s")

                // 观察者处理事件的能力
//                s.request(Long.MAX_VALUE)

                // 4发送过来处理不了了(超过处理范围) -> MissingBackpressureException: create: could not emit value due to lack of requests
//                s.request(3)

                s1 = s

                // s.cancel() 功能(切断事件流管道)同 d.dispose()
            }

            override fun onComplete() {
                LogT.d("接收完成")
            }
        }

        flowable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber)
    }

    var s1: Subscription = object : Subscription {
        override fun cancel() {
        }

        override fun request(n: Long) {
        }
    }


    /**
     * 背压策略
     */
    fun bufferDemo() {
        Flowable.create(FlowableOnSubscribe<Int> { e ->
            for (i in 1..10000) {
                LogT.d("发送$i")
                e.onNext(i)
            }
        },
//                BackpressureStrategy.BUFFER   // 池子大小无限制
//                BackpressureStrategy.DROP   // 丢弃超过池子范围的事件
                BackpressureStrategy.LATEST // 只保留最新的事件
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Int> {
                    override fun onError(t: Throwable?) {
                    }

                    override fun onNext(t: Int?) {
                        LogT.d("接收$t")
                    }

                    override fun onSubscribe(s: Subscription?) {
                        s1 = s!!
                    }

                    override fun onComplete() {
                    }


                })

    }


    /**
     * Interval发送Long型事件，从0开始每隔指定时间将数字加一发出去
     */
    fun interval() {
        Flowable.interval(1, TimeUnit.MICROSECONDS)
                // 添加背压策略
//                .onBackpressureBuffer()
//                .onBackpressureDrop()
                .onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Long> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(s: Subscription) {
                        LogT.d("onSubscribe")
                        s1 = s
                        s.request(Long.MAX_VALUE)
                    }

                    override fun onError(t: Throwable?) {
                        LogT.d("错误${t.toString()}")
                    }

                    override fun onNext(t: Long?) {
                        LogT.d("接收$t")
                        SystemClock.sleep(1000)
                    }
                })
    }


    /**
     * PART9
     */
    fun question1() {
        Flowable.create(FlowableOnSubscribe<Int> { e ->
            for (i in 1..4) {
                LogT.d("发送了$i")
                e.onNext(i)
            }
            LogT.d("发送完成")
            e.onComplete()
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    object : Subscriber<Int> {
                        override fun onSubscribe(s: Subscription?) {
                            LogT.d("开始订阅")
                            s1 = s!!
                            s.request(1)
                        }

                        override fun onComplete() {
                            LogT.d("接受完成")
                        }

                        override fun onNext(t: Int?) {
                            LogT.d("接收到$t")
                            s1.request(1)
                        }

                        override fun onError(t: Throwable?) {
                            LogT.d("接收失败${t.toString()}")
                        }

                    }
                }
    }


    fun request1() {
        Flowable.create(FlowableOnSubscribe<Int> { e ->
            LogT.d("当前请求${e.requested()}")
        }, BackpressureStrategy.ERROR)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    object : Subscriber<Int> {
                        override fun onNext(t: Int?) {
                            LogT.d("onNext: $t")
                        }

                        override fun onComplete() {
                        }

                        override fun onError(t: Throwable?) {
                        }

                        override fun onSubscribe(s: Subscription?) {
                            LogT.d("onSubscribe")
                            s1 = s!!
                        }
                    }
                }




        val flowable = Flowable.create(FlowableOnSubscribe<Int> { e ->

            LogT.d("发送${e.requested()}")
        }, BackpressureStrategy.ERROR)


        val subscriber = object : Subscriber<Int> {
            override fun onNext(t: Int?) {
                LogT.d("接受到$t")
            }

            override fun onError(t: Throwable?) {
                LogT.d("onError" + t.toString())
            }

            override fun onSubscribe(s: Subscription) {
                LogT.d("onSubscribe$s")

            }

            override fun onComplete() {
                LogT.d("接收完成")
            }
        }

        flowable.subscribe(subscriber)


        Flowable.create(FlowableOnSubscribe<Int> { e ->
            LogT.d("发送${e.requested()}")
        }, BackpressureStrategy.ERROR).subscribe { object : Subscriber<Int> {
            override fun onNext(t: Int?) {
                LogT.d("接受到$t")
            }

            override fun onError(t: Throwable?) {
                LogT.d("onError" + t.toString())
            }

            override fun onSubscribe(s: Subscription) {
                LogT.d("onSubscribe$s")

            }

            override fun onComplete() {
                LogT.d("接收完成")
            }
        } }
    }


    fun t() {
        s1.request(1)
    }
}