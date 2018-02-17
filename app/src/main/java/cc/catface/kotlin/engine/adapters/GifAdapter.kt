package cc.catface.kotlin.engine.adapters

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import cc.catface.clibrary.util.ScreenT
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.Gif
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.yhao.commen.download.ProgressDownload
import com.yhao.commen.download.ProgressListener
import org.jetbrains.anko.find
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class GifAdapter(private val mData: List<Gif>, private val rv: RecyclerView) : RecyclerView.Adapter<GifAdapter.GifHolder>() {

    var mHeights: MutableMap<Int, Int> = HashMap()
    var gifDrawable: GifDrawable? = null

    init {
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                pauseGif()
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun onBindViewHolder(holder: GifHolder?, position: Int) {

        Glide.with(holder!!.giv)
                .asBitmap()
                .load(mData[position].img)
                .transition(BitmapTransitionOptions().crossFade(800))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))  // 将图片尺寸缓存到本地[解决加载图片时背景出现浅绿色]
                .apply(RequestOptions.timeoutOf(3000))
                .listener(object : RequestListener<Bitmap> {
                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        val ivWidth = ScreenT.w(holder.giv.context)
                        val ivHeight = ((ivWidth.toDouble() / resource!!.width) * resource.height).toInt()
                        mHeights.put(position, ivHeight)

                        holder.tv_gif.visibility = View.VISIBLE
                        holder.giv.layoutParams.height = ivHeight
                        holder.giv.layoutParams.width = ivWidth
                        holder.tv_gif.text = mData[position].title
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean) = false
                }).into(holder.giv)

        if (mHeights.containsKey(position)) {
            holder.giv.layoutParams.height = mHeights[position]!!
            holder.tv_gif.text = mData[position].title
        }

        holder.giv.setOnClickListener {

            ProgressDownload.downloadPhoto(rv.context, mData[position].img, object : ProgressListener {
                override fun onProgress(readByte: Long, totalByte: Long, done: Boolean) {
                    holder.pb.post {
                        holder.pb.visibility = View.VISIBLE
                    }
                }

                override fun onSave(filePath: String) {
                    val gif = GifDrawable(filePath)
                    holder.giv.post {
                        holder.pb.visibility = View.GONE
                        holder.giv.setImageDrawable(gif)
                    }
                }

            })
        }
    }

    fun pauseGif() {
        if (gifDrawable != null) {
            gifDrawable!!.pause()
        }
    }

    override fun getItemCount() = mData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = GifHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_gif, parent, false))

    class GifHolder(view: View) : RecyclerView.ViewHolder(view) {
        val giv: GifImageView = view.find(R.id.giv)
        val tv_gif: TextView = view.find(R.id.tv_gif)
        val pb: ProgressBar = view.find(R.id.pb)
    }
}