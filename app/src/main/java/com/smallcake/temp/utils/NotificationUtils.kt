package com.smallcake.temp.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.smallcake.temp.MyApplication
import com.smallcake.temp.R

/**
 * MyApplication --  cn.com.smallcake_utils
 * Created by Small Cake on  2017/9/7 17:20.
 * show notification
 * just a simple example
 * maybe you can write more each other notice
 * 新增8.0的兼容性
 */
object NotificationUtils {
    /**
     * 通知消息
     * @param msg
     */
    fun showNotice(title:CharSequence = MyApplication.instance.getString(R.string.app_name),msg: CharSequence) {
        val manager =
            MyApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("1", title, NotificationManager.IMPORTANCE_LOW)
            manager.createNotificationChannel(channel)
            NotificationCompat.Builder(MyApplication.instance, "smallcake")
        } else {
            NotificationCompat.Builder(MyApplication.instance)
        }
        val notification = builder
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setWhen(System.currentTimeMillis())
            .setContentText(msg)
            .setAutoCancel(false)
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            .build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        manager.notify(0, notification)
    }

    /**
     * 显示进度通知消息
     * @param msg
     * @param progress
     */
    fun showNoticeProgress(msg: CharSequence?, progress: Int) {
        val manager =
            MyApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder
        val title = MyApplication.instance.getString(R.string.app_name)
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("1", title, NotificationManager.IMPORTANCE_LOW)
            manager.createNotificationChannel(channel)
            NotificationCompat.Builder(MyApplication.instance, "smallcake")
        } else {
            NotificationCompat.Builder(MyApplication.instance)
        }
        val notification = builder
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setWhen(System.currentTimeMillis())
            .setContentText(msg)
            .setAutoCancel(false)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setProgress(100, progress, false)
            .setOngoing(true).build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        manager.notify(0, notification)
    }
}