package cc.catface.kotlin.engine.adapters

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.Soup
import org.jetbrains.anko.find

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class SoupAdapter(private val mData: List<Soup>?) : RecyclerView.Adapter<SoupAdapter.SoupHolder>() {

    class SoupHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tv: TextView = view.find(R.id.tv_text)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SoupHolder?, position: Int) {
        holder!!.tv.text = mData?.get(position)?.english + "\n\n" + mData?.get(position)?.chinese
    }

    override fun getItemCount() = mData?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = SoupHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_soup, parent, false))
}