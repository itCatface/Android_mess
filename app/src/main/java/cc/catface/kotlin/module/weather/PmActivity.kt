package cc.catface.kotlin.module.weather

import android.app.Dialog
import android.content.Intent
import android.view.View
import android.widget.TextView
import cc.catface.clibrary.base.BaseActivity
import cc.catface.clibrary.project.yiyuan.Constant
import cc.catface.clibrary.util.extension.t
import cc.catface.clibrary.util.net.http.okhttp.full.OHCallback
import cc.catface.clibrary.util.net.http.okhttp.full.OHT
import cc.catface.clibrary.util.view.simple.LinearLayoutMangerScrollSpeed
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.PmRank
import cc.catface.kotlin.engine.DataEngine
import cc.catface.kotlin.engine.adapters.PmRankAdapter
import cc.catface.kotlin.view.group.WeatherTopView
import kotlinx.android.synthetic.main.activity_pm.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import java.util.*


/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class PmActivity : BaseActivity(R.layout.activity_pm) {

    private var mData = ArrayList<PmRank>()
    private var layoutManager: LinearLayoutMangerScrollSpeed? = null


    override fun create() {
        initView()
        initEvent()
        initData()
    }


    private fun initView() {
        wtv_pm.setTitle("空气质量")
        wtv_pm.setRightOneIcon(R.string.fa_search)
        wtv_pm.setRightTwoIcon(R.string.fa_location_arrow)
        rv_pm.setHasFixedSize(true)
        layoutManager = LinearLayoutMangerScrollSpeed(applicationContext)
        rv_pm.layoutManager = layoutManager
    }


    private fun initEvent() {
        wtv_pm.setOnClickListener(WeatherTopView.OnClickListener { view ->
            when (view!!.id) {
                R.id.tv_title -> {
//                    layoutManager!!.scrollToPositionWithOffset(1, 0)    // 头布局到第一个条目时能平滑移动，其他地方是瞬移
                    layoutManager!!.smoothScrollToPosition(rv_pm, null, 1)
                }

                R.id.tv_left_one -> {
                    finish()
                }

                R.id.tv_right_two -> {
                    requestSingleArea("")
                }

                R.id.tv_right_one -> {
                    when (TYPES++ % 3) {
                        1 -> startActivityForResult(Intent(this@PmActivity, ChoseCityNormalActivity::class.java), 0)
                        2 -> startActivityForResult(Intent(this@PmActivity, ChoseCityWeixinActivity::class.java), 0)
                        0 -> startActivityForResult(Intent(this@PmActivity, ChoseCityMeituanActivity::class.java), 0)
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (0 == requestCode && 1 == resultCode) {
            requestSingleArea(data!!.getStringExtra("city"))
        }
    }


    private fun initData() {
        doAsync {
            val data = DataEngine.getPmRankData()

            uiThread {
                rl_loading_pm.visibility = View.GONE
                if (data.isEmpty()) {
                    return@uiThread
                }

                when {
                    null == rv_pm.adapter -> {
                        mData.addAll(data)
                        rv_pm.adapter = PmRankAdapter(mData, rv_pm, layoutManager!!)
                    }
                }
            }
        }
    }

    private fun requestSingleArea(area: String) {
        doAsync {
            val city = if (area.isEmpty()) DataEngine.localCity() else area
            OHT.getInstance().post(Constant.yyPMCity(city), object : OHCallback {
                override fun onSuc(result: String?) {
                    if (!result.isNullOrEmpty()) dialog(result!!)
                }

                override fun onErr(error: String?) {
                    t("err is $error")
                }
            })
        }
    }


    fun dialog(result: String) {
        val view = View.inflate(this, R.layout.dialog_pm_city, null)    // 这里用applicationContext会崩
        val dialog = Dialog(this, R.style.Dialog_Fullscreen)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        rl_loading_pm.visibility = View.GONE

        val tv: TextView = view.find(R.id.tv_pm_city_result)

        val replace = result.replace("[^(\\u4e00-\\u9fa5)]", "")    // 没用啊...
        tv.text = replace
    }

    companion object {
        var TYPES = 1
    }
}

