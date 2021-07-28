package com.smallcake.temp.utils

import android.util.Log

object L {
    fun i(str: String?) {
        Log.i("TAG", str!!)
    }

    fun d(str: String?) {
        Log.d("TAG", str!!)
    }

    fun e(str: String?) {
        Log.e("TAG", str!!)
    }
}