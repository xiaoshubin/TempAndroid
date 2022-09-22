package com.smallcake.temp.module

import android.app.Dialog
import android.content.Context
import com.smallcake.temp.R
import android.os.Bundle
import android.widget.TextView
import android.text.TextUtils
import android.view.View

class LoadDialog @JvmOverloads constructor(mContext: Context, var text: String = "") : Dialog(mContext, R.style.Theme_Ios_Dialog) {
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smallcake_utils_loading_dialog)
        setCanceledOnTouchOutside(false)
        (findViewById<View>(R.id.tv_load_dialog) as TextView).text = if (TextUtils.isEmpty(text)) "加载中..." else text
    }
}