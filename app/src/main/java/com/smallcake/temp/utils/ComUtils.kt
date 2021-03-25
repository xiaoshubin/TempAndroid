package com.smallcake.temp.utils

import com.orhanobut.logger.Logger
import com.smallcake.smallutils.ToastUtil

/**
 * 1.普通的弹出消息
 * 2.日志输出
 */
fun showToast(msg:CharSequence){
    ToastUtil.showLong(msg)
}

fun ldd(msg:String){
    Logger.d(msg)
}
fun lww(msg:String){
    Logger.w(msg)
}
fun lee(msg:String){
    Logger.e(msg)
}
fun ljson(msg:String){
    Logger.json(msg)
}