package com.smallcake.smallutils

import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

/**
 * 控件推拽帮助类
 */
class DragHelper(private val view: View) {
    private var width = 0
    private var height = 0
    private var maxWidth = 0
    private var maxHeight = 0
    private var downX = 0f//按下的x坐标
    private var downY = 0f//按下的y坐标
    private var isDraged = false //View是否被移动过
    private var isDrag = false //判断是拖动还是点击
    private var topMargin = Screen.statusHeight //判断是拖动还是点击

    /**
     * 测量
     */
    fun onMeasure() {
        width = view.measuredWidth
        height = view.measuredHeight
        maxWidth = Screen.width
        maxHeight = Screen.height - DpUtils.dp2px(60f)-topMargin
    }

    /**
     * 触摸事件
     * @param event 事件
     * @return
     */
    fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDrag = false
                    isDraged = false
                    downX = event.x
                    downY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val moveX: Float = event.x - downX
                    val moveY: Float = event.y - downY
                    if (isDraged){
                        isDrag = true //如果已经被拖动过，那么无论本次移动的距离是否为零，都判定本次事件为拖动事件
                    }else{
                        if (abs(moveX) < 3 && abs(moveY) < 3){
                            isDraged = false //如果移动的距离为零，则认为控件没有被拖动过，灵敏度可以自己控制
                        }else{
                            isDraged = true
                            isDrag = true
                        }
                    }
                    var l: Int
                    var r: Int
                    var t: Int
                    var b: Int
                    if (abs(moveX) > 3 || abs(moveY) > 3) {
                        l = (view.left + moveX).toInt()
                        r = l + width
                        t = (view.top + moveY).toInt()
                        b = t + height
                        if (l < 0) {
                            l = 0
                            r = l + width
                        } else if (r > maxWidth) {
                            r = maxWidth
                            l = r - width
                        }
                        if (t < 0) {
                            t = 0
                            b = t + height
                        } else if (b > maxHeight) {
                            b = maxHeight
                            t = b - height
                        }
                        if (isDrag)view.layout(l, t+topMargin, r, b+topMargin)

                    }

                }
                MotionEvent.ACTION_UP ->{
                    if (isDrag)view.isEnabled = false
                    view.isEnabled = true
                }
        }
        return true
    }

}