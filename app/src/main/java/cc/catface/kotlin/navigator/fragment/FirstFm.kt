package cc.catface.kotlin.navigator.fragment

import android.content.Intent
import cc.catface.clibrary.base.BaseFragment
import cc.catface.clibrary.util.extension.startIntent
import cc.catface.kotlin.R
import cc.catface.kotlin.module.function.ad.AdActivity
import cc.catface.kotlin.module.function.iflytek.IflytekActivity
import cc.catface.kotlin.module.function.install.InstallActivity
import cc.catface.kotlin.module.function.intentservice.IntentServiceActivity
import cc.catface.kotlin.module.function.server.ServerActivity
import cc.catface.kotlin.module.music.MusicActivity
import cc.catface.kotlin.module.note.NoteActivity
import cc.catface.kotlin.module.weather.PmActivity
import kotlinx.android.synthetic.main.fragment_first.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class FirstFm : BaseFragment(R.layout.fragment_first) {

    override fun viewCreated() {
        initData()
        initView()
        initEvent()
    }

    private fun initData() {

    }

    private fun initView() {

    }


    private fun initEvent() {
        var i = 0

        bt_test.setOnClickListener {
            tv.text = "asdfadsf" + ++i
            EventBus.getDefault().post("aa" + i)

//            observerDemo()
//            consumerDemo()
//            threadDemo()
//            threadUI(this)
//            mapDemo()
//            flatMapDemo()
//            concatMapDemo()
//            zipDemo1()
//            backPressureDemo()
//            filterDemo()
//            sampleDemo()
//            speedDemo()
//            flowableDemo1()
//            flowableDemo2()
//            bufferDemo()
//            interval()
//            question1()

//            request1()
        }


        bt_pm.setOnClickListener { startIntent(Intent(context, PmActivity::class.java)) }
        bt_note.setOnClickListener { startIntent(Intent(context, NoteActivity::class.java)) }
        bt_ad.setOnClickListener { startIntent(Intent(context, AdActivity::class.java)) }
        bt_server.setOnClickListener { startIntent(Intent(context, ServerActivity::class.java)) }
        bt_music.setOnClickListener { startIntent(Intent(context, MusicActivity::class.java)) }
        bt_iflytek.setOnClickListener { startIntent(Intent(context, IflytekActivity::class.java)) }
        bt_install.setOnClickListener { startIntent(Intent(context, InstallActivity::class.java)) }
        bt_intentService.setOnClickListener { startIntent(Intent(context, IntentServiceActivity::class.java)) }
    }


}