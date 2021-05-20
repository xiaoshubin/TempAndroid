package com.smallcake.smallutils

import android.app.AppOpsManager
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import java.lang.reflect.InvocationTargetException

/**
 * 检测对应权限是否开启，并跳转对应权限设置页面
 */
object PermissUtils {
    /**
     * 检测是否开启【通知权限】
     * 使用：
    if (com.smallcake.smallutils.PermissUtils.isNotificationsEnabled(this)){
    AlertDialog.Builder(this)
    .setTitle("提示")
    .setMessage("未开启通知权限,去开启？")
    .setNegativeButton("确定") { _, _ ->
    com.smallcake.smallutils.PermissUtils.goNotificationsSetPage(this)
    }
    .setPositiveButton("取消",null)
    .show()
    }
     */
    fun isNotificationsEnabled(mContext: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= 24) {
            NotificationManagerCompat.from(mContext).areNotificationsEnabled()
        } else if (Build.VERSION.SDK_INT >= 19) {
            val appOps = mContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val appInfo = mContext.applicationInfo
            val pkg = mContext.applicationContext.packageName
            val uid = appInfo.uid
            try {
                val appOpsClass = Class.forName(AppOpsManager::class.java.name)
                val checkOpNoThrowMethod = appOpsClass.getMethod(
                    "checkOpNoThrow", Integer.TYPE,
                    Integer.TYPE, String::class.java
                )
                val opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION")
                val value = opPostNotificationValue[Int::class.java] as Int
                (checkOpNoThrowMethod.invoke(appOps, value, uid, pkg) as Int
                        == AppOpsManager.MODE_ALLOWED)
            } catch (e: ClassNotFoundException) {
                true
            } catch (e: NoSuchMethodException) {
                true
            } catch (e: NoSuchFieldException) {
                true
            } catch (e: InvocationTargetException) {
                true
            } catch (e: IllegalAccessException) {
                true
            } catch (e: RuntimeException) {
                true
            }
        } else {
            true
        }
    }

    /**
     * 跳转通知设置权限页面
     */
    fun goNotificationsSetPage(context: Context) {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            intent.putExtra(Notification.EXTRA_CHANNEL_ID, context.applicationInfo.uid)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            //这种方案适用于 API21——25
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
        } else {
            //下面这种方案是直接跳转到当前应用的设置界面。
            //https://blog.csdn.net/ysy950803/article/details/71910806
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
        }
        context.startActivity(intent)
    }
}