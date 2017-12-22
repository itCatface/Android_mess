package cc.catface.kotlin.engine.adapters

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import cc.catface.kotlin.App
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.PmRank
import org.jetbrains.anko.find
import java.util.*

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class PmRankAdapter(private var mData: List<PmRank>?, private val rv: RecyclerView?, private val layoutManager: LinearLayoutManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEAD = 1
    private val TYPE_ITEM = 0
    private val TYPE_FOOT = -1

    private var flagPm25 = true
    private var flagAqi = true

    init {
        // 第一次进入页面可隐藏头条目
        layoutManager.scrollToPosition(1)

        rv!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && 0 == layoutManager.findFirstVisibleItemPosition()) {
                    val offset = rv.computeVerticalScrollOffset()

                    /*val viewHead = layoutManager.findViewByPosition(0)
                    val headHeight = viewHead.height*/
                    val headHeight = layoutManager.getChildAt(0).height // px

                    /*if (offset < 84) layoutManager.smoothScrollToPosition(rv, null, 0)
                    else layoutManager.smoothScrollToPosition(rv, null, 1)*/
                    if (offset < headHeight / 2) layoutManager.scrollToPositionWithOffset(0, 0)
                    else layoutManager.scrollToPositionWithOffset(1, 0)
                }
            }
        })
    }


    class HeadHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val bt_default: Button = view.find(R.id.bt_default)
        val bt_pm: Button = view.find(R.id.bt_pm)
        val bt_aqi: Button = view.find(R.id.bt_aqi)
    }

    class FootHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tv_foot: TextView = view.find(R.id.tv_foot)
    }

    class PmRankHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val rl_pm: RelativeLayout = view.find(R.id.rl_pm)
        val tv_city: TextView = view.find(R.id.tv_city)
        val tv_quality: TextView = view.find(R.id.tv_quality)
        val tv_date: TextView = view.find(R.id.tv_date)
        val tv_pm25: TextView = view.find(R.id.tv_pm25)
        val tv_pm10: TextView = view.find(R.id.tv_pm10)
        val tv_aqi: TextView = view.find(R.id.tv_aqi)
    }


    override fun getItemViewType(position: Int) = when (position) {
        0 -> TYPE_HEAD
        itemCount - 1 -> TYPE_FOOT
        else -> TYPE_ITEM
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {

            is HeadHolder -> {
                holder.bt_default.setOnClickListener {
                    flagPm25 = true
                    flagAqi = true
                    holder.bt_pm.text = "PM2.5最低"
                    holder.bt_aqi.text = "空气质量最好"

                    Collections.sort(mData, { t1: PmRank, t2: PmRank ->
                        t1.aqi.toInt() - t2.aqi.toInt()
                    })
                    this.notifyDataSetChanged()
                }


                holder.bt_pm.setOnClickListener {
                    flagAqi = true
                    holder.bt_aqi.text = "空气质量最好"

                    if (flagPm25) {
                        holder.bt_pm.text = "PM2.5最低"
                        Collections.sort(mData, { t1: PmRank, t2: PmRank ->
                            try {
                                t1.pm2_5.toInt() - t2.pm2_5.toInt()
                            } catch (e: NumberFormatException) {
                                -1
                            }
                        })

                    } else {
                        holder.bt_pm.text = "PM2.5最高"
                        Collections.reverse(mData)
                    }

                    this.notifyDataSetChanged()
                    flagPm25 = !flagPm25
                }


                holder.bt_aqi.setOnClickListener {
                    flagPm25 = true
                    holder.bt_pm.text = "PM2.5最低"

                    if (flagAqi) {
                        holder.bt_aqi.text = "空气质量最好"
                        Collections.sort(mData, { t1: PmRank, t2: PmRank ->
                            t1.aqi.toInt() - t2.aqi.toInt()
                        })

                    } else {
                        holder.bt_aqi.text = "空气质量最差"
                        Collections.reverse(mData)
                    }

                    this.notifyDataSetChanged()
                    flagAqi = !flagAqi
                }
            }


            is FootHolder -> {
                holder.tv_foot.text = "已经到底部啦...当前加载数目为:${mData!!.size}"
            }


            is PmRankHolder -> {
                holder.rl_pm.setBackgroundColor(
                        when (mData!![position].quality) {
                            "优质" -> App.ctx!!.resources.getColor(R.color.pmQuality1)
                            "良好" -> App.ctx!!.resources.getColor(R.color.pmQuality2)
                            "轻度污染" -> App.ctx!!.resources.getColor(R.color.pmQuality3)
                            "中度污染" -> App.ctx!!.resources.getColor(R.color.pmQuality4)
                            "重度污染" -> App.ctx!!.resources.getColor(R.color.pmQuality5)
                            "严重污染" -> App.ctx!!.resources.getColor(R.color.pmQuality6)
                            else -> App.ctx!!.resources.getColor(R.color.pmQuality3)
                        })


                holder.tv_city.text = mData!![position].area
                holder.tv_quality.text = mData!![position].quality
                holder.tv_date.text = mData!![position].ct
                holder.tv_pm25.text = "pm2.5:" + mData!![position].pm2_5
                holder.tv_pm10.text = "pm10:" + mData!![position].pm10
                holder.tv_aqi.text = "空气质量指数:" + mData!![position].aqi
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var view: View?
        var holder: RecyclerView.ViewHolder? = null

        when (viewType) {
            TYPE_HEAD -> {
                view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_pm_head, parent, false)
                holder = HeadHolder(view)
            }

            TYPE_FOOT -> {
                view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_pm_foot, parent, false)
                holder = FootHolder(view)
            }

            TYPE_ITEM -> {
                view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_pm_rank, parent, false)
                holder = PmRankHolder(view)
            }
        }

        return holder!!
    }


    override fun getItemCount() = mData!!.size + 1
}