package cc.catface.kotlin.engine.adapters

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import cc.catface.clibrary.util.extension.t
import cc.catface.clibrary.util.sensor.VibratorT
import cc.catface.clibrary.util.view.recyclerview.drag.DragItemCallback
import cc.catface.clibrary.util.view.recyclerview.swipe.SwipeItemLayout
import cc.catface.clibrary.util.view.recyclerview.swipe.SwipeItemListener
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.Note
import cc.catface.kotlin.navigator.App
import org.jetbrains.anko.find
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class NoteAdapter(val mData: MutableList<Note>?, val rv: RecyclerView, val layoutManager: LinearLayoutManager, private val mItemTouchListener: SwipeItemListener) : RecyclerView.Adapter<NoteAdapter.NoteHolder>(), DragItemCallback.DragItemAdapter {
    private val noteDao = App.noteDao

    private var mFirstVisibleItemPosition: Int = 0
    private var mLastVisibleItemPosition: Int = 10


    override fun getItemCount() = mData!!.size
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = NoteHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item, null)) // 使用inflate(R.layout.item, parent, false)会有显示问题
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoteHolder?, position: Int) {

        /* 添加动画 */
        if (holder!!.adapterPosition > mLastVisibleItemPosition) {
            val anim = ObjectAnimator.ofFloat(holder.rl_note, "scaleX", 0f, 1f)
            holder.rl_note.pivotX = 0f  // 伸缩动画相对点
            anim.start()
        } else if (holder.adapterPosition < mFirstVisibleItemPosition)
            ObjectAnimator.ofFloat(holder.rl_note, "alpha", 0f, 1f).start()


        holder.rl_note.setBackgroundColor(when (mData!![position].stars) {
            0 -> App.ctx!!.resources.getColor(R.color.white)
            1 -> App.ctx!!.resources.getColor(R.color.pmQuality1)
            2 -> App.ctx!!.resources.getColor(R.color.pmQuality2)
            3 -> App.ctx!!.resources.getColor(R.color.pmQuality3)
            4 -> App.ctx!!.resources.getColor(R.color.pmQuality4)
            5 -> App.ctx!!.resources.getColor(R.color.pmQuality5)
            else -> App.ctx!!.resources.getColor(R.color.white)
        })


        val formate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val d = formate.format(mData[position].date)
        val date = formate.parse(d)
        holder.tv_date.text = date.month.toString() + "月" + date.day + "日"
        holder.tv_content.text = mData[position].content


        swipeEvent(holder, position)
    }


    class NoteHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rl_note: RelativeLayout = view.find(R.id.rl_note)
        val tv_date: TextView = view.find(R.id.tv_date)
        val tv_content: TextView = view.find(R.id.tv_content)

        val swipe_layout: SwipeItemLayout = view.find(R.id.swipe_layout)
        val left_1: TextView = view.find(R.id.left_1)
        val left_2:TextView = view.find(R.id.left_2)
        val right_menu: TextView = view.find(R.id.right_menu)
    }


    /************************************************ 逻辑 **************************************************/
    private fun top(fromPosition: Int) {
        mData!!.add(0, mData.removeAt(fromPosition))
        notifyItemMoved(fromPosition, 0)
        notifyItemRangeChanged(0, mData!!.size) // 刷新全部position
    }

    private fun deleteData(position: Int) {
        val removeNote = mData!!.removeAt(position)
        this.notifyItemRemoved(position)
        // 删除后position会错乱，调用this.notifyDataSetChanged()能解决但是会使删除条目动画失效，故添加此行
        notifyItemRangeChanged(0, mData.size)

        noteDao!!.delete(removeNote)
    }


    /************************************************ 事件 **************************************************/
    init {
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                VibratorT(rv.context).cancel()
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mFirstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (layoutManager.findLastVisibleItemPosition() == mData!!.size - 1) rv.context.t("已到最深处๑乛◡乛๑")
            }
        })
    }


    /**
     * DragItemCallback
     */
    override fun onMove(startPosition: Int, endPosition: Int) {
        /*if (startPosition == mData!!.size - 1 || endPosition == mData.size - 1) {
            return
        }*/

        if (startPosition < endPosition) {
            for (i in startPosition until endPosition) {
                Collections.swap(mData, i, i + 1)
            }

        } else {
            for (i in startPosition downTo endPosition + 1) {
                Collections.swap(mData, i, i - 1)
            }
        }

        notifyItemMoved(startPosition, endPosition)
    }

    override fun onFinishDrag() {
        notifyItemRangeChanged(0, mData!!.size)
    }

    override fun onSwiped(position: Int, toLeft: Boolean) = if (toLeft) deleteData(position) else top(position)


    /**
     * swipe & click
     */
    private fun swipeEvent(holder: NoteHolder?, position: Int) {
        if (holder!!.layoutPosition % 2 == 0) holder.swipe_layout.isSwipeEnable = false

        // 滑动和单击事件可回调给所在Activity
        holder.left_1.setOnClickListener {
            top(position)
//            mItemTouchListener.onLeftMenuClick(holder.adapterPosition)
//            holder.swipe_layout.close()
        }

        holder.left_2.setOnClickListener {
            VibratorT(rv.context).vibrate(true, 100, 150)
            notifyItemRangeChanged(0, mData!!.size)


            holder.tv_content.isFocusable = true
            holder.tv_content.isFocusableInTouchMode = true
        }

        holder.right_menu.setOnClickListener {
            deleteData(position)
//            mItemTouchListener.onRightMenuClick(holder.adapterPosition)
//            holder.swipe_layout.close()
        }

        holder.rl_note.setOnClickListener {
            mItemTouchListener.onItemClick(holder, position, holder.tv_content.text.toString())
        }
    }
}