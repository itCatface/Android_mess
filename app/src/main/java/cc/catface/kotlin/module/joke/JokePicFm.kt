package cc.catface.kotlin.module.joke

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import cc.catface.clibrary.base.BaseFragment
import cc.catface.clibrary.showSnackbar
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.JokePic
import cc.catface.kotlin.engine.DataEngine
import cc.catface.kotlin.engine.adapters.JokePicAdapter
import kotlinx.android.synthetic.main.fragment_joke_pic.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class JokePicFm : BaseFragment() {


    private var mData = ArrayList<JokePic>()
    private var mPage = 1
    private var mLoading by Delegates.observable(true) { _, _, new ->
        srl_joke_pic.isRefreshing = new
    }


    override fun layoutId() = R.layout.fragment_joke_pic


    override fun viewCreated() {
        initView()
        initEvent()
        initData()
    }


    private fun initView() {
        srl_joke_pic.setColorSchemeColors(Color.RED)
        rv_joke_pic.setHasFixedSize(true)
        rv_joke_pic.layoutManager = LinearLayoutManager(context)
    }


    private fun initEvent() {
        srl_joke_pic.setOnRefreshListener {
            mPage = 1
            initData()
        }

        rv_joke_pic.setOnTouchListener { _, _ ->
            if (!mLoading && !rv_joke_pic.canScrollVertically(1)) {
                mPage++
                initData()
            }

            false
        }
    }


    private fun initData() {
        mLoading = true

        doAsync {
            val data = DataEngine.getJokePicData(mPage)

            uiThread {
                mLoading = false

                if (null == data) {
                    showSnackbar(view as ViewGroup)
                    return@uiThread
                }

                when {
                    null == rv_joke_pic.adapter -> {
                        mData.addAll(data)
                        rv_joke_pic.adapter = JokePicAdapter(mData, rv_joke_pic)
                    }

                    mPage > 1 -> {
                        mData.addAll(data)
                        rv_joke_pic.adapter.notifyItemRangeInserted(mData.size, data.size)
                    }

                    else -> {
                        mData.clear()
                        mData.addAll(data)
                        rv_joke_pic.adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}