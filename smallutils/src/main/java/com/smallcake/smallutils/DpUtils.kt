package com.smallcake.smallutils


/**
 * 单位转换
 */
object DpUtils {
    fun dp2pxFloat(dpValue: Float): Float {
        val scale =
           SmallUtils.context?.resources?.displayMetrics?.density?:0.0f
        return dpValue * scale + 0.5f
    }

    fun dp2px(dpValue: Float): Int {
        val scale = SmallUtils.context?.applicationContext?.resources
            ?.displayMetrics?.density ?:0.0f
        return (dpValue * scale + 0.5f).toInt()
    }
    fun dp2px(dpValue: Int): Int {
        val scale = SmallUtils.context?.applicationContext?.resources
            ?.displayMetrics?.density ?:0.0f
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(pxValue: Float): Int {
        val scale =
            SmallUtils.context?.resources?.displayMetrics?.density ?:0.0f
        return (pxValue / scale + 0.5f).toInt()
    }

}