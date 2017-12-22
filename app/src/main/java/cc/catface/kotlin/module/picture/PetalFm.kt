package cc.catface.kotlin.module.picture

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import cc.catface.clibrary.base.BaseFragment
import cc.catface.clibrary.d
import cc.catface.clibrary.showSnackbar
import cc.catface.clibrary.t
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.Petal
import cc.catface.kotlin.engine.DataEngine
import cc.catface.kotlin.engine.adapters.PetalAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_query.view.*
import kotlinx.android.synthetic.main.fragment_petal.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import java.net.URL
import kotlin.properties.Delegates

/**
 * Created by wyh
 */
class PetalFm : BaseFragment() {

    private var mType: Int = 0
    private var mData = ArrayList<Petal>()
    private var mPage = 1
    private var mLoading by Delegates.observable(true) { _, _, new ->
        srl_pic.isRefreshing = new
    }


    override fun layoutId() = R.layout.fragment_petal

    override fun viewCreated() {
        mType = arguments!!.getInt(TYPE)
        initView()
        initEvent()
        initData()
    }

    private fun initView() {
        srl_pic.setColorSchemeColors(Color.BLUE)
        rv_pic.setHasFixedSize(true)
        rv_pic.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        (rv_pic.layoutManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
    }

    private fun initEvent() {
        srl_pic.setOnRefreshListener {
            mPage = 1
            initData()
        }

        rv_pic.setOnTouchListener { _, _ ->
            if (!mLoading && !rv_pic.canScrollVertically(1)) {
                mPage++
                initData()
            }

            false
        }
    }

    private fun initData() {
        mLoading = true

        doAsync {
            val data = DataEngine.getPetalData(mType, mPage)

            uiThread {
                mLoading = false

                if (null == data) {
                    showSnackbar(view as ViewGroup, "加载失败")
                    return@uiThread
                }

                when {
                    null == rv_pic.adapter -> {
                        mData.addAll(data)
                        initAdapter()
                    }

                    mPage > 1 -> {
                        mData.addAll(data)
                        rv_pic.adapter.notifyItemRangeInserted(mData.size, data.size)
                    }
                    else -> {
                        mData.clear()
                        mData.addAll(data)
                        rv_pic.adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }


    private fun initAdapter() {
        rv_pic.adapter = PetalAdapter(mData)
        (rv_pic.adapter as PetalAdapter).setOnItemCLick(object : PetalAdapter.OnItemClickListener {
            override fun onClick(view: View, url: String) {
                dialog(url)
            }
        })
        (rv_pic.adapter as PetalAdapter).setOnItemLongClickListener(object : PetalAdapter.OnItemLongClickListener {
            override fun onLongClick(view: View) {
                activity!!.t("长按图片了:$view")
            }
        })
    }


    private fun dialog(url: String) {

        val view = View.inflate(context, R.layout.dialog_query, null)
        val dialog = Dialog(context, R.style.Dialog_Fullscreen)
        dialog.setContentView(view)
        dialog.setCancelable(true)

        val animAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f)
        val animRotation = ObjectAnimator.ofFloat(view, "rotation", -25f, 20f, -15f, 10f, -5f, 2f, 0f)
        animRotation.interpolator = DecelerateInterpolator()

        val animSet = AnimatorSet()
        animSet.duration = 600
        animSet.playTogether(animAlpha, animRotation)
        animSet.start()



        dialog.show()


        val iv: ImageView = view.find(R.id.iv_dialog)
        val bt_dialog_like: Button = view.find(R.id.bt_dialog_like)

        Glide.with(this).load(url).into(iv)

        iv.setOnClickListener {


            val animAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.2f)
            val animRotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 5f, -10f, 15f, -20f, 25f, -30f)
            animRotation.interpolator = AccelerateDecelerateInterpolator()

            val animSet = AnimatorSet()
            animSet.duration = 600
            animSet.playTogether(animAlpha, animRotation)
            animSet.start()
            animSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    dialog.dismiss()
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }

            })

        }

        iv.setOnLongClickListener {
            view.ll_dialog.visibility = View.VISIBLE
            val anim = ObjectAnimator.ofFloat(view.ll_dialog, "alpha", 0f, 1f)
            anim.duration = 300
            anim.start()
            true
        }

        bt_dialog_like.setOnClickListener {
            activity!!.t("喜欢")
        }
    }


    companion object {
        val TYPE = "type"
        fun getInstance(type: Int): PetalFm {
            val fragment = PetalFm()
            val bundle = Bundle()
            bundle.putInt(TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}