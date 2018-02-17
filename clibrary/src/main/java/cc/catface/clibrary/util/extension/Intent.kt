package cc.catface.clibrary.util.extension

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
fun Activity.startIntent(intent: Intent, finish: Boolean = false) {
    this.startActivity(intent)
    if (finish) this.finish()
}

fun Fragment.startIntent(intent: Intent, finish: Boolean = false) {
    this.activity?.startActivity(intent)
    if (finish) this.activity?.finish()
}