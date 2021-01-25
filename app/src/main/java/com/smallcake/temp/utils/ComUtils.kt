package com.smallcake.temp.utils

import android.widget.Toast
import com.smallcake.temp.MyApplication

/**
 * 普通的弹出消息
 * @param msg CharSequence
 */
fun showToast(msg:CharSequence){
    Toast.makeText(MyApplication.instance,msg,Toast.LENGTH_LONG).show()
}