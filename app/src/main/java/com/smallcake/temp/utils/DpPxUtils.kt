package com.smallcake.temp.utils

import com.smallcake.temp.MyApplication

object DpPxUtils {
    fun dp2pxFloat(dpValue: Float): Float {
        val scale =
            MyApplication.instance.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    fun dp2px(dpValue: Float): Int {
        val scale = MyApplication.instance.applicationContext.resources
            .displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(pxValue: Float): Int {
        val scale =
            MyApplication.instance.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

}