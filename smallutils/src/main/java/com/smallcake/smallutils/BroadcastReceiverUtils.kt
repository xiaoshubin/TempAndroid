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

    /**
     * 息屏 亮屏 广播
     * @property listenerOn Function0<Unit>?
     * @property listenerOff Function0<Unit>?
     */
    class ScreenStatusReceiver : BroadcastReceiver() {
        var listenerOn: (()->Unit)? = null
        var listenerOff: (()->Unit)? = null

        fun setScreenOn(screenOn:()->Unit){
            this.listenerOn = screenOn
        }
        fun setScreenOff(screenOff:()->Unit){
            this.listenerOff = screenOff
        }
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_SCREEN_ON) {
                Log.i(TAG, "亮屏")
                listenerOn?.invoke()
            }
            if (action == Intent.ACTION_SCREEN_OFF) {
                Log.i(TAG, "息屏")
                listenerOff?.invoke()
            }
        }

        companion object {
            private const val TAG = "ScreenStatusReceiver"
        }
    }

    /**
     * 注册 息屏 亮屏 广播
     */
    private fun registSreenStatusReceiver(context: Context) {
        val mScreenStatusReceiver = ScreenStatusReceiver()
        val screenStatusIF = IntentFilter()
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON)
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF)
        context.registerReceiver(mScreenStatusReceiver, screenStatusIF)
        //关闭倒计时
        mScreenStatusReceiver.setScreenOff{

        }
        mScreenStatusReceiver.setScreenOn{

        }


    }


}