package cc.catface.kotlin.module.function.ad

import cc.catface.clibrary.base.BaseActivity
import cc.catface.clibrary.util.extension.t
import cc.catface.clibrary.util.view.viewpager.banner.RecyclerViewBannerBase
import cc.catface.kotlin.R
import kotlinx.android.synthetic.main.activity_ad.*

class AdActivity : BaseActivity(R.layout.activity_ad) {

    private val imgUrls = arrayOf("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1398415679,1254740245&fm=27&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=73725640,1948341253&fm=27&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=947313244,2662783106&fm=27&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1144942459,690886074&fm=27&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2994369606,3652748422&fm=27&gp=0.jpg")

    private val titleList = arrayListOf("a1", "b2", "c3", "d4", "e5")

    override fun create() {
        initView()
    }

    private fun initView() {
        ad_1.initBannerImageView(imgUrls.toList(), object : RecyclerViewBannerBase.OnBannerItemClickListener {
            override fun onItemClick(position: Int) {
                t(titleList[position])
            }

        }, RecyclerViewBannerBase.OnBannerScrolledListener { position ->
            tv_1.text = titleList[position % titleList.size]
        })

        ad_2.initBannerImageView(imgUrls.toList(), object : RecyclerViewBannerBase.OnBannerItemClickListener {
            override fun onItemClick(position: Int) {
                t(titleList[position])
            }

        }, RecyclerViewBannerBase.OnBannerScrolledListener { position ->
            tv_2.text = titleList[position % titleList.size]

        })

        ad_3.initBannerImageView(imgUrls.toList(), object : RecyclerViewBannerBase.OnBannerItemClickListener {
            override fun onItemClick(position: Int) {
                t(titleList[position])
            }

        }, RecyclerViewBannerBase.OnBannerScrolledListener { position ->
            tv_3.text = titleList[position % titleList.size]

        })

        ad_4.initBannerImageView(imgUrls.toList(), object : RecyclerViewBannerBase.OnBannerItemClickListener {
            override fun onItemClick(position: Int) {
                t(titleList[position])
            }

        }, RecyclerViewBannerBase.OnBannerScrolledListener { position ->
            tv_4.text = titleList[position % titleList.size]

        })
    }
}
