package cc.catface.clibrary.util.extension

import android.content.Context
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
val DEBUG = true
val DEFAULT_TAG = "wyh"

fun d(msg: String) = d(DEFAULT_TAG, msg)

fun d(tag: String, msg: String) {
    if (DEBUG) Log.d(tag, msg)
}

fun Context.t(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    if (DEBUG) Toast.makeText(this, msg, duration).show()
}

fun Fragment.t(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    if (DEBUG) Toast.makeText(this.activity?.applicationContext, msg, duration).show()
}

/* Request */
fun n(map: Map<String, Any>) = n("", map)

fun n(url: String, map: Map<String, Any>) {
    if (DEBUG) {
        if (!TextUtils.isEmpty(url)) d("url -> $url")

        for ((k, v) in map) d("$k -> $v")
    }
}