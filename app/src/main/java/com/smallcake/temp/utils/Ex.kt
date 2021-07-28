package com.yx.jiading.utils

import android.view.View
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Date:2021/6/3 15:55
 * Author:SmallCake
 * Desc:各种状态转换
 *
 * */
//是否显示，避免大量的 if(true)View.VISIBLE else View.GONE
fun Boolean.visiable():Int{
    return if (this) View.VISIBLE else View.GONE
}
//是否显示反向,避免大量的 if(true)View.GONE else View.VISIBLE
fun Boolean.visiableReverse():Int{
    return if (this) View.GONE else View.VISIBLE
}

//是否签名，是否完成等"Y","N"字段串判断，避免大量if(this==null) false else this == "Y"
fun String?.isY():Boolean{
   return if(this==null) false else this == "Y"
}
fun String?.is1():Boolean{
   return if(this==null) false else this == "1"
}
//方便单个参数转RequestBody，请求体方式提交
fun String.toRbJson():RequestBody{
   return this.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}
//方便单个参数转RequestBody，请求体方式提交
fun String.toRbForm():RequestBody{
   return this.toRequestBody("form-data".toMediaTypeOrNull())
}

fun List<*>?.sizeNull():Int{
    return this?.size ?: 0
}




