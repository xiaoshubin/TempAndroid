package com.smallcake.smallutils.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


/**
 * Date:2021/6/30 10:58
 * Author:SmallCake
 * Desc:无法横向滚动的ViewPager
 **/
class NoScrollViewPager:ViewPager {
    constructor(context: Context):super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)
    override fun onTouchEvent(arg0: MotionEvent?): Boolean {
        return false
    }
    override fun onInterceptTouchEvent(arg0: MotionEvent?): Boolean {
        return false
    }
    override fun setCurrentItem(item: Int) {
        //去除页面切换时的滑动翻页效果
        super.setCurrentItem(item, false)
    }
}