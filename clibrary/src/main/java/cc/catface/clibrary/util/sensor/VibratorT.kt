package cc.catface.clibrary.util.sensor

import android.app.Service
import android.content.Context
import android.os.Vibrator

/**
 * Created by wyh
 */
class VibratorT constructor(ctx: Context) {

    private val vibrator = (ctx.getSystemService(Service.VIBRATOR_SERVICE)) as Vibrator


    fun vibrate(duration: Long) {
        vibrator.vibrate(duration)
    }

    fun vibrate(repeat: Boolean, vararg pattern: Long) {
        vibrator.vibrate(pattern, if (repeat) 0 else -1)
    }

    fun cancel() {
        vibrator.cancel()
    }
}