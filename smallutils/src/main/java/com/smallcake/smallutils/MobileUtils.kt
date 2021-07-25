package com.smallcake.smallutils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build

object MobileUtils {
    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     */
    fun callPhone(activity: Activity, phoneNum:String){
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        activity.startActivity(intent)
    }
    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     * 需要申请权限
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     */
    fun callPhonePermission(activity: Activity, phoneNum:String){
        val intent = Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        activity.startActivity(intent)
    }
    /**
     * 获取手机制造商名称
     */
    fun getMobileFactoryName(): String? {
        return Build.MANUFACTURER
    }

    /**
     * 手机的可见名称
     */
    fun getMobileName(): String? {
        return Build.MODEL
    }
}