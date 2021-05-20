package com.smallcake.smallutils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

class BroadcastReceiverUtils {
    /**
     * 监听【每分钟】时间变化的广播
     */
    private val mTimeTickReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (Intent.ACTION_TIME_TICK == action) {
                Log.i("BroadcastReceiverUtils", "时间变化了")
            }
        }
    }

    /**
     * 注册广播：页面中注册时间广播
     */
    private fun registerTimeTick(context: Context) {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_TIME_TICK)
            context.registerReceiver(mTimeTickReceiver, filter)
    }

    /**
     * 记得取消注册，一般在onDestroy()内调用
     */
    private fun unregisterTimeTick(context: Context) {
        context.unregisterReceiver(mTimeTickReceiver)

    }


}