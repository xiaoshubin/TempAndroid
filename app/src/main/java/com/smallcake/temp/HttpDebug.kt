package com.smallcake.temp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smallcake.smallutils.DpPxUtils
import com.smallcake.smallutils.ShapeCreator
import com.smallcake.temp.utils.ldd
import com.smallcake.temp.utils.showToast

/**
 * 网络数据监控调试器
 */
class HttpDebug(mContext: Context, parent: ViewGroup) {
    var isDebug:Boolean = BuildConfig.DEBUG//是否开启了调试模式
    init {
        if (isDebug){
            val httpDebugText = LayoutInflater.from(mContext).inflate(R.layout.http_debug_text, parent, true)
            val tv = httpDebugText.findViewById<View>(R.id.debug_tv)
            ShapeCreator.create()
                .setPadding(16)
                .setCornerRadius(10f)
                .setStateEnabled(true)
                .setSolidColor(Color.GRAY)
                .setSolidPressColor(Color.DKGRAY)
                .setStrokeColor(Color.CYAN)
                .setStrokePressColor(Color.MAGENTA)
                .setStrokeWidth(2)
                .setStateTextColorEnabled(true)
                .setTextColor(Color.BLACK)
                .setTextPressColor(Color.WHITE)
                .into(tv)
            val dp16 = DpPxUtils.dp2px(16f)
            val layoutParams = (tv.layoutParams as ViewGroup.MarginLayoutParams)
            layoutParams.topMargin = dp16
            tv.layoutParams = layoutParams
            tv.setOnClickListener{
                showToast("开发模式弹框~！")
                ldd("开发模式弹框~！")
            }
        }

    }



}