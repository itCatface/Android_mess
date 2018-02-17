package cc.catface.kotlin.navigator.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.widget.Button
import cc.catface.clibrary.base.BaseActivity
import cc.catface.clibrary.util.extension.d
import cc.catface.clibrary.util.extension.replace
import cc.catface.kotlin.R
import cc.catface.kotlin.navigator.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class MainActivity : BaseActivity(R.layout.activity_main), View.OnClickListener {

    private val mFragments = arrayOf(FirstFm(), SecondFm(), ThirdFm(), FourthFm(), FifthFm())
    private var mMainTabs: Array<Button>? = null

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) fun onEvent(event: String) {


        /*val tl = supportFragmentManager.findFragmentByTag("SecondFm").tl_second
        when (event) {

            "gone" -> {
                if (tl_second.visibility != View.GONE)
                    animOut(tl)
            }
            "visible" -> {
                if (tl_second.visibility != View.VISIBLE)
                    animIn(tl)
            }
        }*/
    }

    fun animOut(view: View) {
        val animSet = AnimatorSet()
        animSet.playTogether(ObjectAnimator.ofFloat(view, "alpha", 1f, 0f),
                ObjectAnimator.ofFloat(view, "translationY", 0f, -view.height.toFloat()))
        animSet.setDuration(300).start()
        animSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                view.visibility = View.GONE
            }

        })
    }

    fun animIn(view: View) {
        view.visibility = View.VISIBLE
        val animSet = AnimatorSet()
        animSet.playTogether(ObjectAnimator.ofFloat(view, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(view, "translationY", -view.height.toFloat(), 0f))
        animSet.setDuration(300).start()
    }


    private var currentIndex = 0
    private var selectedIndex = -1
    override fun create() {
        EventBus.getDefault().register(this)
        initView()
        initEvent()

//        bt_6.setOnClickListener { startActivity(Intent(this, PmActivity::class.java)) }
    }

    private fun initView() {
        mMainTabs = arrayOf(bt_1, bt_2, bt_3, bt_4, bt_5)

        supportFragmentManager.beginTransaction().add(R.id.fl, mFragments[0], mFragments[0].javaClass.name.substring(mFragments[0].javaClass.name.lastIndexOf("") + 1)).show(mFragments[0]).commit()
        mMainTabs!![0].isSelected = true
    }

    private fun initEvent() {
        for (bt in mMainTabs!!) bt.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.bt_1 -> selectedIndex = 0
            R.id.bt_2 -> selectedIndex = 1
            R.id.bt_3 -> selectedIndex = 2
            R.id.bt_4 -> selectedIndex = 3
            R.id.bt_5 -> selectedIndex = 4
        }

        val transaction = supportFragmentManager.beginTransaction()

        if (selectedIndex != currentIndex) {
            transaction.hide(mFragments[currentIndex])

            if (!mFragments[selectedIndex].isAdded)
                transaction.add(R.id.fl, mFragments[selectedIndex], mFragments[selectedIndex].javaClass.name.substring(mFragments[0].javaClass.name.lastIndexOf("") + 1))

            transaction.show(mFragments[selectedIndex]).commit()

            mMainTabs!![currentIndex].isSelected = false
            mMainTabs!![selectedIndex].isSelected = true

            currentIndex = selectedIndex
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    /**
     * fragment
     */
    private var fragmentFlag = 0

    private fun replace(index: Int, tag: String = "default") {
        if (fragmentFlag != index) {
            replace(R.id.fl, mFragments[index], tag)
            d("fragment tag is: " + tag)
            fragmentFlag = index
        }
    }

    fun getFirstFm() {
        val findFragmentByTag = supportFragmentManager.findFragmentByTag("FirstFm")
        d("findFragmentByTagis $findFragmentByTag")
//        supportFragmentManager.findFragmentByTag("FirstFm") as FirstFm
    }

    fun getSeconFm() = supportFragmentManager.findFragmentByTag("SecondFm") as SecondFm

}



