package cc.catface.clibrary.util.extension

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
fun wh(activity: Activity): Array<Int> {
    val metrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(metrics)
    return arrayOf(metrics.widthPixels, metrics.heightPixels)
}

fun wh(view: View): Array<Int> {
    val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    view.measure(w, h)
    return arrayOf(view.measuredWidth, view.measuredHeight)
}

fun px2dp(ctx: Context, value: Float) = (value / ctx.resources.displayMetrics.density + 0.5f).toInt()

fun px2sp(ctx: Context, value: Float) = (value / ctx.resources.displayMetrics.scaledDensity).toInt()

fun dp2px(ctx: Context, value: Float) = (value * ctx.resources.displayMetrics.density + 0.5f).toInt()

fun sp2px(ctx: Context, value: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, ctx.resources.displayMetrics)


/* 控件View接收事件的能力 */
fun View.ability(flag: Boolean) {
    this.isEnabled = flag
    this.isClickable = flag
    this.isFocusable = flag
    this.isFocusableInTouchMode = flag
}