package com.yhao.module.pic

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.Joke
import org.jetbrains.anko.find
import java.util.regex.Pattern
import java.util.regex.Pattern.compile


/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class JokeAdapter(private val mData: List<Joke>?) : RecyclerView.Adapter<JokeAdapter.JokeHolder>() {

    class JokeHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tv_text: TextView = view.find(R.id.tv_text)
        val tv_title: TextView = view.find(R.id.tv_title)
    }

    override fun onBindViewHolder(holder: JokeHolder, position: Int) {
        holder.tv_title.text = mData?.get(position)?.title
        holder.tv_text.text = compile("<[^>]+>", Pattern.CASE_INSENSITIVE).matcher(mData?.get(position)?.text).replaceAll("")   // 过滤掉原数据的html标签
    }

    override fun getItemCount() = mData?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = JokeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_joke, parent, false))
}