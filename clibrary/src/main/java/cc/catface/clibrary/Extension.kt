package cc.catface.clibrary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import cc.catface.clibrary.util.LogT.d
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by wyh
 */
val DEBUG = true
val USE_SOUT = true
val DEFAULT_TAG = "catface_debug"

fun Context.toast(msg: CharSequence) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun AppCompatActivity.replace(id: Int, fm: Fragment, tag: String = "default") {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    transaction.replace(id, fm, tag).commit()
    supportFragmentManager.executePendingTransactions()
}

fun showSnackbar(viewGroup: ViewGroup, text: String = "加载失败", duration: Int = 1000) {
    val snack = Snackbar.make(viewGroup, text, duration)
    snack.view.setBackgroundColor(Color.BLUE)
    snack.show()
}


/**------------------------------------------ 打印(日志&土司) ------------------------------------------*/
fun Any.d(msg: String) = d(DEFAULT_TAG, msg)

fun Any.d(tag: String, msg: String) {
    if (DEBUG)
        if (USE_SOUT) System.out.println(tag + " --> " + msg)
        else Log.d(tag, msg)
}

fun Context.t(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    if (DEBUG) Toast.makeText(this, msg, duration).show()
}


/* 3. Request */
fun n(map: Map<String, Any>) = n("", map)

fun n(url: String, map: Map<String, Any>) {
    if (DEBUG) {
        if (!TextUtils.isEmpty(url)) d("url -> $url")

        for ((k, v) in map) d("$k -> $v")
    }
}


/**
 * Created by yhao on 17-9-4.
 * 扩展委托
 */


/**
 *  * notNull委托，只能被赋值一次，如果第二次赋值就会抛异常
 */
fun <T> notNullSingleValue():
        ReadWriteProperty<Any?, T> = NotNullSingleValueVar()

/**
 *  SharedPreferences 委托  自动存取
 */
fun <T : Any> preference(context: Context, name: String, default: T):
        ReadWriteProperty<Any?, T> = Preference(context, name, default)


private class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("already initialized")
    }
}


private class Preference<T>(val context: Context, val name: String, val default: T)
    : ReadWriteProperty<Any?, T> {

    val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun <T> findPreference(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can not be saved into Preferences")
        }
        res as T
    }

    @SuppressLint("CommitPrefEdits")
    private fun <U> putPreference(name: String, value: U) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }.apply()
    }
}



