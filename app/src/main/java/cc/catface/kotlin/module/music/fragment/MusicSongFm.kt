package cc.catface.kotlin.module.music.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cc.catface.clibrary.base.BaseFragment
import cc.catface.clibrary.d
import cc.catface.clibrary.t
import cc.catface.kotlin.R
import cc.catface.kotlin.module.music.domain.Song
import cc.catface.kotlin.module.music.engine.AudioUtils
import cc.catface.kotlin.module.music.engine.MusicSongAdapter
import kotlinx.android.synthetic.main.fragment_music_song.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by catfaceWYH --> tel|wechat|qq 13012892925
 */
class MusicSongFm : BaseFragment() {
    override fun layoutId() = R.layout.fragment_music_song

    private var mData: MutableList<Song>? = null

    override fun viewCreated() {


        initView()
        searchLocalMusics()

    }

    private fun initView() {
        rv_music.setHasFixedSize(true)
        rv_music.layoutManager = LinearLayoutManager(context)
    }

    private fun searchLocalMusics() {
        doAsync {
            mData = AudioUtils.getAllSongs(context!!)
            for (song in mData!!) {
                d("" + song.fileName)
            }

            uiThread {
                initAdapter()

            }
        }
    }


    private fun initAdapter() {
        rv_music.adapter = MusicSongAdapter(mData!!)
        (rv_music.adapter as MusicSongAdapter).setOnItemCLick(object : MusicSongAdapter.OnItemClickListener {
            override fun onClick(view: View, songName: String) {
                activity!!.t(songName)
            }
        })
    }
}