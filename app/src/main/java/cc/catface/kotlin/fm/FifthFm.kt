package cc.catface.kotlin.fm

import android.content.Context
import android.net.wifi.WifiManager
import cc.catface.clibrary.base.BaseFragment
import cc.catface.kotlin.R
import cc.catface.kotlin.module.server.NanoHTTPDServer
import kotlinx.android.synthetic.main.fragment_fifth.*

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class FifthFm : BaseFragment() {
    override fun viewCreated() {
        tv_ip.text = getLocalIpStr()

        val server = NanoHTTPDServer(9093)
        server.start()
    }

    override fun layoutId() = R.layout.fragment_fifth


    private fun getLocalIpStr(): String {
        val wifiManager = context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        return int2IpAddr(wifiInfo.ipAddress)
    }


    private fun int2IpAddr(ip: Int) = ((ip and 0xFF).toString() + "."
            + (ip shr 8 and 0xFF) + "."
            + (ip shr 16 and 0xFF) + "."
            + (ip shr 24 and 0xFF))

}