package com.smallcake.temp.utils

import android.widget.Toast
import com.orhanobut.logger.Logger
import com.smallcake.temp.MyApplication

/**
 * 普通的弹出消息
 * @param msg CharSequence
 */
fun showToast(msg:CharSequence){
    Toast.makeText(MyApplication.instance,msg,Toast.LENGTH_LONG).show()
}

fun ldd(msg:String){
    Logger.d(msg)
}
fun lee(msg:String){
    Logger.e(msg)
}
fun ljson(msg:String){
    Logger.json(msg)
}