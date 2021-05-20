@file:Suppress("DEPRECATION")

package com.smallcake.temp.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.smallcake.temp.MyApplication
import java.io.File

object AppUtils {
    //获取应用包名
     fun getAppPackageName(): String = MyApplication.instance.packageName

    //获取版本号
    fun getVersionCode(): Int {
        var versioncode = 0
        try {
            val pm: PackageManager = MyApplication.instance.packageManager
            val pi: PackageInfo = pm.getPackageInfo(getAppPackageName(), 0)
            versioncode = pi.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versioncode
    }

    //获取版本名称
    fun getVersionName(): String {
        var versionName = ""
        try {
            val pm: PackageManager = MyApplication.instance.packageManager
            val pi: PackageInfo = pm.getPackageInfo(getAppPackageName(), 0)
            versionName = pi.versionName
            if (versionName == null || versionName.isEmpty()) return ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionName
    }

    /**
     * 安装APK
     * @param activity Activity
     * @param downloadApk String 下载apk后的手机上的文件地址
     */
    fun installApk(activity: Activity, downloadApk: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val file = File(downloadApk)
//        ldd("安装路径==$downloadApk")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val apkUri = FileProvider.getUriForFile(activity,"${getAppPackageName()}.fileprovider",file)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val uri = Uri.fromFile(file)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
        }
        activity.startActivity(intent)
    }

    /**
     * 获取进程名称
     */
    private fun getProcessName(context: Context, pid:Int):String?{
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (runningApp in runningApps) {
            if (runningApp.pid==pid)return runningApp.processName
        }
        return null
    }

}