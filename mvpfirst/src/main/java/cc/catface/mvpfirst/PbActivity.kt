package cc.catface.mvpfirst

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import cc.catface.clibrary.base.BaseActivity
import cc.catface.clibrary.util.extension.d
import kotlinx.android.synthetic.main.activity_pb.*
import org.jetbrains.anko.toast

class PbActivity : BaseActivity(R.layout.activity_pb) {

    val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            d("what is: ${msg?.what}")
            when (msg?.what) {
                1 -> {
                    d("count is: $count")
                    pb_1.progress = count
                }
            }
        }
    }


    var count = 0
    override fun create() {

        pb_1.max = 100

        pb_1.progress = 30

        var mRunFlag = false


        bt_start.setOnClickListener {
            pb_1.progress = 40

            SystemClock.sleep(1000)


            while (mRunFlag) {
                mHandler.obtainMessage(1).sendToTarget()

                count++

                if (count >= 100) {
                    toast("加载完成")
                    mRunFlag = false
                }

                SystemClock.sleep(80)
            }
        }
    }
}
