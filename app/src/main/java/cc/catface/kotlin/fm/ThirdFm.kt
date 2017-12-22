package cc.catface.kotlin.fm

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import cc.catface.clibrary.base.BaseFragment
import cc.catface.clibrary.util.view.viewpager.animation.ViewPagerAnim
import cc.catface.kotlin.R
import cc.catface.kotlin.module.picture.PetalFm
import kotlinx.android.synthetic.main.fragment_third.*

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class ThirdFm : BaseFragment() {
    private val tabs = mutableListOf("小清新" to 35, "文艺范" to 36, "大胸妹" to 34, "大长腿" to 38, "小翘臀" to 40, "性感妹" to 37, "黑丝袜" to 39)

    override fun layoutId() = R.layout.fragment_third

    override fun viewCreated() {
        vp_third.offscreenPageLimit = tabs.size // 不加左右滑动几下就会爆炸

        vp_third.setPageTransformer(true, ViewPagerAnim.DEPTH_PAGE)   // 给ViewPager加点滑动效果
        vp_third.adapter = PicPageAdapter(childFragmentManager)
        tl_third.setupWithViewPager(vp_third)
    }


    private inner class PicPageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount() = tabs.size

        override fun getPageTitle(position: Int) = tabs[position].first

        override fun getItem(position: Int) = PetalFm.getInstance(tabs[position].second)
    }
}