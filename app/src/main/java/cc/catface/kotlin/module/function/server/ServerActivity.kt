package cc.catface.kotlin.module.function.server

import android.content.Context
import android.net.wifi.WifiManager
import cc.catface.clibrary.base.BaseActivity
import cc.catface.kotlin.R
import cc.catface.kotlin.view.group.WeatherTopView
import kotlinx.android.synthetic.main.activity_server.*

class ServerActivity : BaseActivity(R.layout.activity_server) {

    private val server = NanoHTTPDServer(9093)

    override fun create() {
        initView()
        initEvent()
        initData()
    }


    private fun initView() {
        wtv_server.setTitle("nanohttpd服务器")
    }


    private fun initEvent() {
        wtv_server.setOnClickListener(WeatherTopView.OnClickListener { view ->
            when (view?.id) {
                R.id.tv_left_one -> {
                    finish()
                }
            }
        })
    }


    private fun initData() {
        tv_ip.text = getLocalIpStr()

        server.start()
    }


    private fun getLocalIpStr(): String {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        return int2IpAddr(wifiInfo.ipAddress)
    }


    private fun int2IpAddr(ip: Int) = ((ip and 0xFF).toString() + "" + (ip shr 8 and 0xFF) + "" + (ip shr 16 and 0xFF) + "" + (ip shr 24 and 0xFF))


    /* 退出需停止server or --> java.net.BindException: bind failed: EADDRINUSE (Address already in use) */
    override fun onStop() {
        super.onStop()
        server.stop()
    }
}
