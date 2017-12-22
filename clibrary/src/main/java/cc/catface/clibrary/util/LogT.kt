package cc.catface.clibrary.util

import android.content.Context
import android.support.annotation.UiThread
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

/**
 * Created by wyh
 */
object LogT {

    val DEBUG = true

    val DEFAULT_TAG = "catface_debug"


    /* 1. Log */
    /* verbose | info | debug | warn | error | assert[wtf] */
    fun d(msg: String) = d(DEFAULT_TAG, msg)

    fun d(tag: String, msg: String) {
        if (DEBUG)
            Log.d(tag, msg)
    }


    /* 2. Toast */
    val THREAD_MAIN_NAME = "main"

    fun t(ctx: Context, msg: String) = t(ctx, msg, Toast.LENGTH_SHORT)

    fun t(ctx: Context, msg: String, duration: Int) {
        if (DEBUG)
            if (THREAD_MAIN_NAME == Thread.currentThread().name)
                Toast.makeText(ctx, msg, duration).show()

    }


    /* 3. Request */
    fun n(map: Map<String, Any>) =  n("", map)

    fun n(url: String, map: Map<String, Any>) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(url)) {
                d("url -> $url")
            }

            for ((k, v) in map) {
                d("$k -> $v")
            }
        }
    }
}