package cc.catface.kotlin.module.joke

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import cc.catface.clibrary.base.BaseFragment
import cc.catface.clibrary.showSnackbar
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.Soup
import cc.catface.kotlin.engine.DataEngine
import cc.catface.kotlin.engine.adapters.SoupAdapter
import kotlinx.android.synthetic.main.fragment_soup.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 * Created by wyh
 */
class SoupFm : BaseFragment() {


    companion object {
        val SPEED_RATIO = 0.691f    // RecyclerView滑动速率
    }


    private var mData = ArrayList<Soup>()
    private var mPage = 1
    private var mLoading by Delegates.observable(true) { _, _, new ->
        srl_soup.isRefreshing = new
    }


    override fun layoutId() = R.layout.fragment_soup


    override fun viewCreated() {
        initView()
        initEvent()
        initData()
    }


    private fun initView() {
        srl_soup.setColorSchemeColors(Color.BLACK)
        rv_soup.setHasFixedSize(true)
        rv_soup.layoutManager = object : LinearLayoutManager(context) {
            /* 控制RecyclerView的滑动速度 */
            override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
                val value = super.scrollVerticallyBy((SPEED_RATIO * dy).toInt(), recycler, state)
                return if (value == (SPEED_RATIO * dy).toInt()) dy else value
            }
        }
    }


    private fun initEvent() {
        srl_soup.setOnRefreshListener {
            mPage = 1
            initData()
        }

        rv_soup.setOnTouchListener { _, _ ->
            if (!mLoading && !rv_soup.canScrollVertically(1)) {
                mPage++
                initData()
            }

            false
        }
    }


    private fun initData() {
        mLoading = true

        doAsync {
            val data = DataEngine.getSoupData()

            uiThread {
                mLoading = false

                if (null == data) {
                    showSnackbar(view as ViewGroup)
                    return@uiThread
                }

                when {
                    null == rv_soup.adapter -> {
                        mData.addAll(data)
                        rv_soup.adapter = SoupAdapter(mData)
                    }

                    mPage > 1 -> {
                        mData.addAll(data)
                        rv_soup.adapter.notifyItemRangeInserted(mData.size, data.size)
                    }

                    else -> {
                        mData.clear()
                        mData.addAll(data)
                        rv_soup.adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}