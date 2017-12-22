package cc.catface.kotlin.engine.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.catface.kotlin.R
import org.jetbrains.anko.find

/**
 * Created by catfaceWYH --> tel|wechat|qq 13012892925
 */
class ChoseCityAdapter(val mData: List<String>) : RecyclerView.Adapter<ChoseCityAdapter.ChoseCityHolder>() {
    override fun getItemCount() = mData.size
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ChoseCityHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_city, null))

    override fun onBindViewHolder(holder: ChoseCityHolder?, position: Int) {
        holder!!.tv_city.text = mData[position]
    }

    inner class ChoseCityHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_city: TextView = view.find(R.id.tv_city)
    }
}