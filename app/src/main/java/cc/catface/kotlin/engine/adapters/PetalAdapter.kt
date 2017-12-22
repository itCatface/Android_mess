package cc.catface.kotlin.engine.adapters

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cc.catface.clibrary.util.ScreenT
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.Petal
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import org.jetbrains.anko.find

/**
 * Created by wyh
 */
class PetalAdapter(private val datas: List<Petal>?) : RecyclerView.Adapter<PetalAdapter.PetalHolder>() {

    private var mHeights: MutableMap<Int, Int> = HashMap()

    override fun onBindViewHolder(holder: PetalHolder, position: Int) {
        Glide.with(holder.iv_pic)
                .asBitmap()
                .load(datas?.get(position)?.thumb)
                .transition(BitmapTransitionOptions().crossFade(800))
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Bitmap>?, p3: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(bitmap: Bitmap, p1: Any?, p2: Target<Bitmap>?, p3: DataSource?, p4: Boolean): Boolean {
                        val imageViewWidth = (ScreenT.w(holder.iv_pic.context)
                                - holder.iv_pic.context.resources.getDimensionPixelSize(R.dimen.picCardMargin) * 4) / 2
                        val imageViewHeight: Int = ((imageViewWidth.toDouble() / bitmap.width) * bitmap.height).toInt()
                        mHeights.put(position, imageViewHeight)
                        holder.iv_pic.layoutParams.height = imageViewHeight
                        holder.iv_pic.layoutParams.width = imageViewWidth
                        return false
                    }
                })
                .into(holder.iv_pic)


        if (mHeights.containsKey(position)) {
            holder.iv_pic.layoutParams.height = mHeights[position]!!
        }


        /* 添加事件 */
        holder.iv_pic.setOnClickListener({
            if (mOnItemClickListener != null) mOnItemClickListener!!.onClick(holder.iv_pic, datas?.get(position)?.thumb!!)
        })

        holder.iv_pic.setOnLongClickListener({
            if (mOnItemLongClickListener != null) mOnItemLongClickListener!!.onLongClick(holder.iv_pic)
            true
        })
    }


    override fun getItemCount() = datas?.size ?: 0
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = PetalHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_pic, parent, false))
    class PetalHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val iv_pic: ImageView = view.find(R.id.iv_joke_pic)
    }


    /********************************************* 点击 & 长按事件回调 *********************************************/
    interface OnItemClickListener {
        fun onClick(view: View, url: String)
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