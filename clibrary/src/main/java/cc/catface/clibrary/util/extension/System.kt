package cc.catface.clibrary.util.extension

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import java.io.*

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
fun getProcessName(cxt: Context, pid: Int): String? {
    val manager = cxt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningApps = manager.runningAppProcesses ?: return null

    return runningApps
            .firstOrNull { it.pid == pid }
            ?.processName
}

fun getRunningProcessCount(ctx: Context): Int {
    val am = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val infos = am.runningAppProcesses
    return infos.size
}

fun getAvailRAM(ctx: Context): Long {
    val am = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val outInfo = ActivityManager.MemoryInfo()
    am.getMemoryInfo(outInfo)
    return outInfo.availMem
}

fun getTotalRAM(ctx: Context): Long {
    //		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    //		MemoryInfo outInfo = new MemoryInfo();
    //		am.getMemoryInfo(outInfo);
    //		return outInfo.totalMem;
    var fis: FileInputStream? = null
    try {
        val file = File("/proc/meminfo")
        fis = FileInputStream(file)
        val br = BufferedReader(InputStreamReader(fis))
        val line = br.readLine()
        val stringBuffer = StringBuffer()
        line.toCharArray()
                .filter { it in '0'..'9' }
                .forEach { stringBuffer.append(it) }

        return java.lang.Long.parseLong(stringBuffer.toString()) * 1024

    } catch (e: Exception) {
        e.printStackTrace()

    } finally {
        if (fis != null) {
            try {
                fis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    return 0
}


fun isServiceRunning(ctx: Context, classname: String?): Boolean {

    if (TextUtils.isEmpty(classname) || null == classname) {
        return false
    }

    val am = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val infos = am.getRunningServices(1000)
    return infos
            .map { it.service.className }
            .contains(classname)
}