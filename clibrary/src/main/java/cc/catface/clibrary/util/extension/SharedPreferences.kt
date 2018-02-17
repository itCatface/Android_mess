package cc.catface.clibrary.util.extension

import android.content.Context


/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 *
 * @desc commit() | apply() -- 同步有布尔返回值 | 异步无返回值
 */
val SharedPreferencesPath = "spConfig"

fun putInt(ctx: Context, key: String, value: Int = 0) = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().putInt(key, value).apply()

fun getInt(ctx: Context, key: String, defValue: Int = 0) = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).getInt(key, defValue)

fun putLong(ctx: Context, key: String, value: Long = 0) = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().putLong(key, value).apply()

fun getLong(ctx: Context, key: String, defValue: Long = 0) = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).getLong(key, defValue)

fun putFloat(ctx: Context, key: String, value: Float = 0f) = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().putFloat(key, value).apply()

fun getFloat(ctx: Context, key: String, defValue: Float = 0f) = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).getFloat(key, defValue)

fun putBoolean(ctx: Context, key: String, value: Boolean = false) = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply()

fun getBoolean(ctx: Context, key: String, defValue: Boolean = false) = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).getBoolean(key, defValue)

fun putString(ctx: Context, key: String, value: String = "") = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().putString(key, value).apply()

fun getString(ctx: Context, key: String, defValue: String = "") = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).getString(key, defValue)

fun remove(ctx: Context, key: String) = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().remove(key).apply()

fun clear(ctx: Context) = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE).edit().clear().apply()
