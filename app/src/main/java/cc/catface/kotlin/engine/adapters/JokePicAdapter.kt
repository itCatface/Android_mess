package cc.catface.kotlin.engine.adapters

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cc.catface.clibrary.d
import cc.catface.clibrary.util.ScreenT
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.JokePic
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.find

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class JokePicAdapter(private val mData: List<JokePic>?, rv: RecyclerView?) : RecyclerView.Adapter<JokePicAdapter.JokePicHolder>() {


    class JokePicHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val iv: ImageView = view.find(R.id.iv_joke_pic)
        val tv: TextView = view.find(R.id.tv_joke_pic)
    }


    private var mHeights: MutableMap<Int, Int> = HashMap()

    init {
        var flag = true

        rv!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                d("dx is $dx, dy is $dy")

                // 隐藏顶部
                if (flag)
                    if (dy in 5..9) {
                        EventBus.getDefault().post("gone")
                        flag = !flag
                    }


                if (!flag)
                    if (dy in -9..-5) {
                        EventBus.getDefault().post("visible")
                        flag = !flag
                    }
            }
        })
    }


    override fun onBindViewHolder(holder: JokePicHolder?, position: Int) {

        holder!!.tv.text = mData!![position].title

        Glide.with(holder.iv)
                .asBitmap()
                .load(mData[position].img)
                .transition(BitmapTransitionOptions().crossFade(800))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
//                .apply(RequestOptions.centerCropTransform())              // 适应宽高最小值
//                .apply(RequestOptions.fitCenterTransform())               // 适应宽高最大值
//                .thumbnail(0.1f)                                          // 预加载像素数// 将图片尺寸缓存到本地[解决加载图片时背景出现浅绿色]
                .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))  // 预加载占位图
                .apply(RequestOptions.errorOf(R.mipmap.ic_launcher_round))  // 错误占位图
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean) = false

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        val ivWidth = ((ScreenT.w(holder.iv.context)) - holder.iv.context.resources.getDimensionPixelSize(R.dimen.picCardMargin) * 4)
                        val ivHeight = ((ivWidth.toDouble() / resource!!.width) * resource.height).toInt()

                        mHeights.put(position, ivHeight)

                        holder.iv.layoutParams.width = ivWidth
                        holder.iv.layoutParams.height = ivHeight
                        return false
                    }
                }).into(holder.iv)

    }

    override fun getItemCount() = mData!!.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = JokePicHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_joke_pic, parent, false))
}