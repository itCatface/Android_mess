package cc.catface.kotlin.module.music

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import cc.catface.clibrary.base.BaseActivity
import cc.catface.kotlin.R
import cc.catface.kotlin.module.music.fragment.MusicAlbumFm
import cc.catface.kotlin.module.music.fragment.MusicArtistFm
import cc.catface.kotlin.module.music.fragment.MusicSongFm
import kotlinx.android.synthetic.main.activity_music.*

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class MusicActivity : BaseActivity(R.layout.activity_music) {

    private val tabs = arrayOf("歌曲", "歌手", "专辑")
    val fms = arrayOf(MusicSongFm(), MusicArtistFm(), MusicAlbumFm())

    override fun create() {

        /**
         * 为TabLayout添加分割线
         */
        val ll = tl_music.getChildAt(0) as LinearLayout
        ll.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        ll.dividerPadding = 40  // 控制TabLayout分割线的宽度
        ll.dividerDrawable = ContextCompat.getDrawable(this, R.drawable.shape_line)


        vp_music.offscreenPageLimit = tabs.size
        vp_music.adapter = MusicPageAdapter(supportFragmentManager)
        tl_music.setupWithViewPager(vp_music)
    }

    private inner class MusicPageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount() = tabs.size

        override fun getPageTitle(position: Int) = tabs[position]

        override fun getItem(position: Int) = fms[position]
    }
}