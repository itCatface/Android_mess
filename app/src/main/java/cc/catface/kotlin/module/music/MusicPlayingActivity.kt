package cc.catface.kotlin.module.music

import android.view.View
import cc.catface.clibrary.base.BaseActivity
import cc.catface.kotlin.R
import kotlinx.android.synthetic.main.activity_music_playing.*

/**
 * Created by catfaceWYH --> tel|wechat|qq 13012892925
 */
class MusicPlayingActivity:BaseActivity(), View.OnClickListener {


    override fun layoutId() = R.layout.activity_music_playing

    override fun create() {
        initView()
        initEvent()
        initData()
    }

    private fun initView() {

    }

    private fun initEvent() {
        iv_order.setOnClickListener(this)
        iv_prev.setOnClickListener(this)
        iv_play.setOnClickListener(this)
        iv_next.setOnClickListener(this)
        iv_list.setOnClickListener(this)
    }

    private fun initData() {

    }


    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.iv_order->{

            }
            R.id.iv_prev->{

            }
            R.id.iv_play->{

            }
            R.id.iv_next->{

            }
            R.id.iv_list->{

            }

        }
    }
}