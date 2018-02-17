package cc.catface.kotlin.navigator.fragment

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import cc.catface.clibrary.base.BaseFragment
import cc.catface.kotlin.R
import cc.catface.kotlin.module.joke.*
import kotlinx.android.synthetic.main.fragment_second.*

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class SecondFm : BaseFragment(R.layout.fragment_second) {

    private val tabs = arrayOf("段子", "鸡汤", "图片", "GIF", "视频")
    val fms = arrayOf(JokeFm(), SoupFm(), JokePicFm(), GifFm(), VideoFm())

    override fun viewCreated() {
        /* 为TabLayout添加分割线 */
        val ll = tl_second.getChildAt(0) as LinearLayout
        ll.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        ll.dividerPadding = 40  // 控制TabLayout分割线的宽度
        ll.dividerDrawable = ContextCompat.getDrawable(context!!, R.drawable.shape_line)


        vp_second.offscreenPageLimit = tabs.size
        vp_second.adapter = JokePageAdapter(childFragmentManager)
        tl_second.setupWithViewPager(vp_second)
    }


    private inner class JokePageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount() = tabs.size

        override fun getPageTitle(position: Int) =tabs[position]

        override fun getItem(position: Int) = fms[position]
    }
}