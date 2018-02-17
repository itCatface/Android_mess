package cc.catface.clibrary.util.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
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


/******************************************* PART TWO --> ANDROID ************************************************/
val SP_PATH = "spConfig"

fun Context.putInt(key: String, value: Int = 0) = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().putInt(key, value).apply()

fun Context.getInt(key: String, defValue: Int = 0) = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).getInt(key, defValue)

fun Context.putLong(key: String, value: Long = 0) = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().putLong(key, value).apply()

fun Context.getLong(key: String, defValue: Long = 0) = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).getLong(key, defValue)

fun Context.putFloat(key: String, value: Float = 0f) = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().putFloat(key, value).apply()

fun Context.getFloat(key: String, defValue: Float = 0f) = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).getFloat(key, defValue)

fun Context.putBoolean(key: String, value: Boolean = false) = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply()

fun Context.getBoolean(key: String, defValue: Boolean = false) = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).getBoolean(key, defValue)

fun Context.putString(key: String, value: String = "") = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().putString(key, value).apply()

fun Context.getString(key: String, defValue: String = "") = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).getString(key, defValue)

fun Context.remove(key: String) = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().remove(key).apply()

fun Context.clear() = getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().clear().apply()



