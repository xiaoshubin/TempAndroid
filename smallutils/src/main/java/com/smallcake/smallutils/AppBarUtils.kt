package com.smallcake.smallutils

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlin.math.abs

/**
 * Date: 2020/8/31
 * author: SmallCake
 * 根据appbar的滚动来逐渐改变控件颜色
 */
object AppBarUtils {
     fun setColorChange(appBar: AppBarLayout,viewTarget: View,startColor: String,endColor: String) {
        appBar.getChildAt(0).viewTreeObserver
            .addOnGlobalLayoutListener {
                //获取AppBarLayout最大滑动距离
                val scrollRange = appBar.totalScrollRange
                appBar.addOnOffsetChangedListener(OnOffsetChangedListener { _: AppBarLayout, verticalOffset: Int ->
                    //获得滑动具体，计算比例
                    val scrollDistance = abs(verticalOffset)
                    val scrollPercentage = scrollDistance.toFloat() / scrollRange
                    val argbEvaluator = ArgbEvaluator() //渐变色计算类
                    val currentLastColor = argbEvaluator.evaluate(scrollPercentage,Color.parseColor(startColor),Color.parseColor(endColor)) as Int
                    viewTarget.setBackgroundColor(currentLastColor)
                })
            }
    }

    //从灰色到白色
    fun setColorGrayWhite(appBar: AppBarLayout, viewTarget: View) {
        setColorChange(
            appBar,
            viewTarget,
            "#f5f5f5",
            "#ffffff"
        )
    }
}