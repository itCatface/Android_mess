package cc.catface.clibrary.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
abstract class BaseActivity : AppCompatActivity() {

    /**------------------------- life cycle of activity -------------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        create()
    }


    /**------------------------- abstract methods -------------------------*/
    abstract fun layoutId(): Int

    abstract fun create()


}

