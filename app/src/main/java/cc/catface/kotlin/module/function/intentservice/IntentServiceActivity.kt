package cc.catface.kotlin.module.function.intentservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import cc.catface.kotlin.R
import kotlinx.android.synthetic.main.activity_intent_service.*


class IntentServiceActivity : AppCompatActivity() {

    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mBroadcastReceiver: MyBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_service)

        //注册广播
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        mBroadcastReceiver = MyBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_TYPE_THREAD)
        mLocalBroadcastManager!!.registerReceiver(mBroadcastReceiver!!, intentFilter)

        initView()
    }

    fun initView() {
        tv_status!!.text = "线程状态：未运行"
        pb!!.max = 100
        pb!!.progress = 0
        tv_progress!!.text = "0%"

        bt_start.setOnClickListener {
            val intent = Intent(this@IntentServiceActivity, TestIntentService::class.java)
            startService(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //注销广播
        mLocalBroadcastManager!!.unregisterReceiver(mBroadcastReceiver!!)
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {

                ACTION_TYPE_THREAD -> {
                    //更改UI
                    val progress = intent.getIntExtra("progress", 0)
                    tv_status!!.text = "线程状态：" + intent.getStringExtra("status")
                    pb!!.progress = progress
                    tv_progress!!.text = progress.toString() + "%"
                    if (progress >= 100) {
                        tv_status!!.text = "线程结束"
                    }
                }
            }
        }
    }

    companion object {
        val ACTION_TYPE_THREAD = "action.type.thread"
    }
}