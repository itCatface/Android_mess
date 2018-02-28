package cc.catface.kotlin.module.function

import cc.catface.clibrary.base.BaseActivity
import cc.catface.clibrary.util.view.dialog.DialogT
import cc.catface.kotlin.R
import kotlinx.android.synthetic.main.activity_dialog.*
import org.jetbrains.anko.toast

class DialogActivity : BaseActivity(R.layout.activity_dialog) {

    val mDialog = DialogT.getInstance(this)

    override fun create() {
        dialogTest()
    }


    private fun dialogTest() {
        bt_dialog.setOnClickListener {
            mDialog.notice(R.mipmap.ic_launcher, "我是标题", "我是通知内容", "确定", "取消", object : DialogT.Callback {
                override fun positive() {
                    toast("确定")
                }

                override fun negative() {
                    toast("取消")
                }
            })
        }


        bt_items.setOnClickListener {
            mDialog.items(R.mipmap.ic_launcher, "列表对话框", arrayOf("艾希", "易", "贾克斯"), object : DialogT.Callback {
                override fun positive() {

                }

                override fun negative() {

                }
            })
        }
    }
}
