package cc.catface.kotlin.module.music.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

/**
 * Created by catfaceWYH --> tel|wechat|qq 13012892925
 */
class MusicService : Service {

    var mPlayer: MediaPlayer? = null
    var mFlag = false


    constructor() {
        if (null == mPlayer) mPlayer = MediaPlayer()
    }


    class MusicController : Binder() {

    }


    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}