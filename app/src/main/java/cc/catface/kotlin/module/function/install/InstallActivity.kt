package cc.catface.kotlin.module.function.install

import cc.catface.clibrary.base.BaseActivity
import cc.catface.clibrary.util.extension.d
import cc.catface.kotlin.R
import com.stericson.RootTools.RootTools
import kotlinx.android.synthetic.main.activity_install.*

class InstallActivity : BaseActivity(R.layout.activity_install) {
    override fun create() {
        bt_root.setOnClickListener {
            d("开始root")
            Runtime.getRuntime().exec("su")
        }

        bt_reboot.setOnClickListener {
            d("即将重启")
            RootTools.sendShell("sleep 5;reboot;", 30000)
        }

        bt_install.setOnClickListener {
            d("开始静默安装")
            RootTools.sendShell("pm install /sdcard/1518454778604.apk", 30000)
            RootTools.sendShell("am start -n com.yys.mobilerobot2/com.yys.mobilerobot2.module.start.WelcomeActivity", 30000)
            RootTools.sendShell("pm uninstall com.yys.mobilerobot2", 30000)
        }


    }


}
