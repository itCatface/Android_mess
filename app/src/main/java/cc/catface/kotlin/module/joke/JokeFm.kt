package cc.catface.kotlin.module.joke

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import cc.catface.clibrary.base.BaseFragment
import cc.catface.clibrary.showSnackbar
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.Joke
import cc.catface.kotlin.engine.DataEngine
import com.yhao.module.pic.JokeAdapter
import kotlinx.android.synthetic.main.fragment_joke.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 * Created by wyh
 */
class JokeFm : BaseFragment() {

    private var mData = ArrayList<Joke>()
    private var mPage = 1
    private var mLoading by Delegates.observable(true) { _, _, new ->
        srl_joke.isRefreshing = new
    }

    override fun layoutId() = R.layout.fragment_joke

    override fun viewCreated() {
        initView()
        initEvent()
        initData()
    }


    private fun initView() {
        srl_joke.setColorSchemeColors(Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.GRAY)
        rv_joke.setHasFixedSize(true)   // 当条目高度固定时选中避免重复requestLayout()
        rv_joke.layoutManager = LinearLayoutManager(context)
    }


    private fun initEvent() {
        srl_joke.setOnRefreshListener {
            mPage = 1
            initData()
        }

        rv_joke.setOnTouchListener { _, _ ->
            if (!mLoading && !rv_joke.canScrollVertically(1)) { // 1[能否向上滚动(加载)] && -1[能否向下滚动(刷新)]
                mPage++
                initData()
            }

            false
        }
    }


    private fun initData() {
        mLoading = true
        doAsync {
            val data = DataEngine.getJokeData(mPage)

            uiThread {
                mLoading = false

                if (null == data) {
                    showSnackbar(view as ViewGroup)
                    return@uiThread
                }

                when {
                    null == rv_joke.adapter -> {
                        mData.addAll(data)
                        rv_joke.adapter = JokeAdapter(mData)
                    }

                    mPage > 1 -> {
                        mData.addAll(data)
                        rv_joke.adapter.notifyItemRangeInserted(mData.size, data.size)
                    }

                    else -> {
                        mData.clear()
                        mData.addAll(data)
                        rv_joke.adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}