package com.smallcake.smallutils

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.widget.TextView

/**
 * Date:2021/5/28
 * author:SmallCake
 * 60秒倒计时
 **/
class CDTimer: CountDownTimer {
    private  var tv: TextView? = null
    constructor(textView:TextView) : super(60000,1000) {
        this.tv = textView
    }

    @SuppressLint("SetTextI18n")
    override fun onTick(millisUntilFinished: Long) {
        if (tv == null) {
            cancel()
            return
        }

        tv?.text = "${millisUntilFinished /1000}s后重新验证"
        tv?.isEnabled = false
    }

    override fun onFinish() {
        if (tv == null) {
            cancel()
            return
        }
        tv?.text = "获取验证码"
        tv?.isEnabled = true

    }
}
//15分钟倒计时
class LearnCDTimer: CountDownTimer {
    private  var tv: TextView? = null
    constructor(textView: TextView):super(60000*15,1000) {
        this.tv = textView
    }
    @SuppressLint("SetTextI18n")
    override fun onTick(millisUntilFinished: Long) {
        if (tv == null) {
            cancel()
            return
        }
        val timeLong = millisUntilFinished / 1000
        val minutes = timeLong/60
        val secound = timeLong%60
        val secoundStr:String = if (secound<10) "0$secound" else secound.toString()

        tv?.text = "倒计时 "+ if(minutes>0)"${minutes}:$secoundStr" else secoundStr
        tv?.isEnabled = false
    }

    override fun onFinish() {
        if (tv == null) {
            cancel()
            return
        }
        tv?.text = "完成"
        tv?.isEnabled = true

    }
}