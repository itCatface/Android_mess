package cc.catface.kotlin.navigator

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import cc.catface.clibrary.util.extension.notNullSingleValue
import cc.catface.kotlin.domain.dao.Dao
import cc.catface.kotlin.domain.greendao_gen.NoteDao

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class App : Application() {

    /**---------------------------- all switches ----------------------------*/
    companion object {
        var SWITCH_TEST = true


        var instance: App by notNullSingleValue()
        @SuppressLint("StaticFieldLeak")
        var ctx: Context? = null

        var noteDao: NoteDao? = null
    }


    override fun onCreate() {
        super.onCreate()

        instance = this
        ctx = applicationContext
        noteDao = getNoteDao()

//        RecognizerT.init(ctx)
    }

    private fun getNoteDao() = Dao.noteDao(ctx!!)


    /*64k*/
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}