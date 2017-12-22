package cc.catface.kotlin.module.weather

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import cc.catface.clibrary.base.BaseActivity
import cc.catface.clibrary.t
import cc.catface.kotlin.R
import cc.catface.kotlin.engine.DataEngine
import cc.catface.kotlin.module.weather.indexbarsupport.*
import cc.catface.kotlin.view.group.WeatherTopView
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration
import kotlinx.android.synthetic.main.activity_chose_city.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class ChoseCityMeituanActivity : BaseActivity() {
    override fun layoutId() = R.layout.activity_chose_city


    private var mManager: LinearLayoutManager = LinearLayoutManager(this)
    private var mAdapter: CityAdapter? = CityAdapter()
    private var mHeaderAdapter: HeaderRecyclerAndFooterWrapperAdapter? = null
    private var mDatas: MutableList<CityBean>? = null
    private var mDecoration: SuspensionDecoration? = null


    override fun create() {
        initView()
        initEvent()
        initData()
    }


    fun initView() {
        wtv_city.setTitle("请选择城市")
        wtv_city.setRightOneIcon(R.string.fa_refresh)

        initIndexBar()
    }


    private fun initIndexBar() {
        mHeaderAdapter = object : HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            override fun onBindHeaderHolder(holder: ViewHolder, headerPos: Int, layoutId: Int, o: Any) {
                holder.setText(R.id.tv_city, o as String)
            }
        }
        mHeaderAdapter!!.setHeaderView(R.layout.item_city, "如有城市遗缺，请联系我们哦...")

        mDecoration = SuspensionDecoration(this).setHeaderViewCount(mHeaderAdapter!!.headerViewCount)

        rv_city!!.layoutManager = mManager
        rv_city!!.adapter = mHeaderAdapter
        rv_city!!.addItemDecoration(mDecoration)
        rv_city!!.addItemDecoration(DividerItemDecoration(this@ChoseCityMeituanActivity, DividerItemDecoration.VERTICAL_LIST)) // 如果add两个则按先后顺序依次渲染

        ib_city!!.setPressedShowTextView(tv_city_notice).setLayoutManager(mManager)
    }


    fun initEvent() {
        wtv_city.setOnClickListener(WeatherTopView.OnClickListener { v ->
            mDatas = null


            when (v.id) {
                R.id.tv_left_one -> finish()

                R.id.tv_right_one -> {

                }
            }
        })

        (mAdapter as CityAdapter).setOnItemClickListener { result ->
            val intent = Intent()
            intent.putExtra("city", result)
            setResult(1, intent)
            finish()
        }
    }


    private fun initData() {
        doAsync {
            val data = DataEngine.getPmCityList()
            if (null == data || data.isEmpty()) {
                t("当前不可查询，请稍候重试...")
                return@doAsync
            }

            uiThread {
                window.decorView.postDelayed({
                    /* 获取bean */
                    mDatas = ArrayList()
                    for (i in data.indices) {
                        val cityBean = CityBean()
                        cityBean.city = data[i]
                        mDatas!!.add(cityBean)
                    }

                    ib_city!!.setSourceData(mDatas).setHeaderViewCount(mHeaderAdapter!!.headerViewCount).invalidate()

                    mAdapter!!.setDatas(mDatas)
                    mHeaderAdapter!!.notifyDataSetChanged()
                    mDecoration!!.setDatas(mDatas)
                }, 200)
            }
        }
    }
}