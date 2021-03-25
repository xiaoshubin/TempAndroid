package com.smallcake.smallutils

import android.annotation.SuppressLint
import android.content.Context

/**
 * MyApplication --  com.smallcake.utils
 * Created by Small Cake on  2017/9/13 10:28.
 * 必须初始化
 */
class SmallUtils private constructor() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        /**
         * 初始化工具类
         */
        fun init(context: Context) {
            mContext = context
        }

        /**
         * 获取Context
         */
        val context: Context?
            get() {
                if (mContext != null) return mContext
                throw NullPointerException("SmallUtils should init first")
            }
    }

    init {
        throw UnsupportedOperationException("SmallUtils must be init")
    }
}