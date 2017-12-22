package cc.catface.clibrary.base

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus

/**
 * Created by wyh
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

