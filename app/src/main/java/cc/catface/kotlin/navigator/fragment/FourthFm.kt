package cc.catface.kotlin.navigator.fragment

import cc.catface.clibrary.base.BaseFragment
import cc.catface.clibrary.util.extension.wh
import cc.catface.kotlin.R
import kotlinx.android.synthetic.main.fragment_fourth.*

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class FourthFm : BaseFragment(R.layout.fragment_fourth) {

    override fun viewCreated() {
        tv.text =""+ wh(activity!!)[0] + " || " + wh(activity!!)[1]

        tv2.text = "" + wh(tv2)[0] + " || " + wh(tv2)[1]

    }
}