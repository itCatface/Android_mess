package cc.catface.kotlin.module.joke

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import cc.catface.clibrary.base.BaseFragment
import cc.catface.clibrary.util.extension.showSnackbar
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.Gif
import cc.catface.kotlin.engine.DataEngine
import cc.catface.kotlin.engine.adapters.GifAdapter
import kotlinx.android.synthetic.main.fragment_gif.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class GifFm : BaseFragment(R.layout.fragment_gif) {

    private var mData = ArrayList<Gif>()
    private var mPage = 1
    private var mLoading by Delegates.observable(true) { _, _, new ->
        srl_gif.isRefreshing = new
    }


    override fun viewCreated() {
        initView()
        initEvent()
        initData()
    }


    private fun initView() {
        srl_gif.setColorSchemeColors(Color.BLUE)
        rv_gif.setHasFixedSize(true)
        rv_gif.layoutManager = LinearLayoutManager(context)
    }


    private fun initEvent() {
        srl_gif.setOnRefreshListener {
            mPage = 1
            initData()
        }

        rv_gif.setOnTouchListener { _, _ ->
            if (!mLoading && !rv_gif.canScrollVertically(1)) {
                mPage++
                initData()
            }

            false
        }
    }


    private fun initData() {
        mLoading = true

        doAsync {
            val data = DataEngine.getGifData(mPage)

            uiThread {
                mLoading = false
                if (data.isEmpty()) {
                    showSnackbar(view as ViewGroup)
                    return@uiThread
                }

                when {
                    null == rv_gif.adapter -> {
                        mData.addAll(data)
                        rv_gif.adapter = GifAdapter(mData, rv_gif)
                    }

                    mPage > 1 -> {
                        mData.addAll(data)
                        rv_gif.adapter.notifyItemRangeInserted(mData.size, data.size)
                    }

                    else -> {
                        mData.clear()
                        mData.addAll(data)
                        rv_gif.adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}