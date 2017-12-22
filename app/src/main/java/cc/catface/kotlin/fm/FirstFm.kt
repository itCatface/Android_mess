package cc.catface.kotlin.fm

import android.content.Intent
import cc.catface.clibrary.base.BaseFragment
import cc.catface.clibrary.t
import cc.catface.clibrary.util.cat.RxJavaT.request1
import cc.catface.clibrary.util.cat.RxJavaT.t
import cc.catface.clibrary.util.sensor.camera.CameraDiyActivity
import cc.catface.clibrary.util.view.viewpager.banner.RecyclerViewBannerBase
import cc.catface.kotlin.R
import cc.catface.kotlin.module.note.NoteActivity
import cc.catface.kotlin.module.weather.PmActivity
import kotlinx.android.synthetic.main.fragment_first.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by wyh
 */
class FirstFm : BaseFragment() {
    override fun layoutId() = R.layout.fragment_first

    private val imgUrls = arrayOf("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1398415679,1254740245&fm=27&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=73725640,1948341253&fm=27&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=947313244,2662783106&fm=27&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1144942459,690886074&fm=27&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2994369606,3652748422&fm=27&gp=0.jpg")

    private val titleList = arrayListOf("空气质量", "备忘录", "33", "44", "55")

    override fun viewCreated() {
        initData()
        initView()
        initEvent()
    }

    private fun initData() {

    }

    private fun initView() {
        banner.initBannerImageView(imgUrls.toList(), object : RecyclerViewBannerBase.OnBannerItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> context!!.startActivity(Intent(context, PmActivity::class.java))
                    1 -> context!!.startActivity(Intent(context, NoteActivity::class.java))
                    2 -> context!!.startActivity(Intent(context, CameraDiyActivity::class.java))
                    else -> context!!.t(titleList[position])
                }
            }

        }, RecyclerViewBannerBase.OnBannerScrolledListener { position -> tv_title.text = titleList[position % titleList.size] })
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

            request1()
        }


        bt_once.setOnClickListener {
            t()
        }

        bt_weather.setOnClickListener {
        }


    }


}