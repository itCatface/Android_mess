package cc.catface.kotlin.module.music.engine

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.catface.kotlin.R
import cc.catface.kotlin.module.music.domain.Song
import org.jetbrains.anko.find

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class MusicSongAdapter(val mData: List<Song>) : RecyclerView.Adapter<MusicSongAdapter.MusicHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = MusicHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_music_song, null))
    override fun getItemCount() = mData.size
    override fun onBindViewHolder(holder: MusicHolder?, position: Int) {
        val songName= mData[position].fileName!!.replace(".mp3", "")

        holder!!.tv_song_name.text = songName

        /* 添加事件 */
        holder.tv_song_name.setOnClickListener({
            if (mOnItemClickListener != null) mOnItemClickListener!!.onClick(holder.tv_song_name, songName)
        })
    }

    class MusicHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_song_name: TextView = view.find(R.id.tv_song_name)
    }


    /********************************************* 点击 & 长按事件回调 *********************************************/
    interface OnItemClickListener {
        fun onClick(view: View, songName:String)
    }

    var mOnItemClickListener: OnItemClickListener? = null
    fun setOnItemCLick(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }


    interface OnItemLongClickListener {
        fun onLongClick(view: View)
    }

    var mOnItemLongClickListener: OnItemLongClickListener? = null
    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener
    }
}