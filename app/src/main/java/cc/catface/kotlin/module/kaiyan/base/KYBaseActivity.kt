package cc.catface.kotlin.module.kaiyan.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
abstract class KYBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())


    }


    abstract fun layoutId(): Int

}
