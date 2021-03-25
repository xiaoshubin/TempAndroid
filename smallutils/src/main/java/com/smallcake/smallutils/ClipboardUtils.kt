package com.smallcake.smallutils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils

/**
 * MyApplication --  com.smallcake.utils
 * Created by Small Cake on  2018/3/20 14:21.
 * 剪贴板工具类
 */
object ClipboardUtils {
    //复制内容
    fun copy(context: Context, msg: String?) {
        if (TextUtils.isEmpty(msg)) {
            ToastUtil.showLong("复制的内容为空")
            return
        }
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, msg)
        cm.setPrimaryClip(clipData)
        ToastUtil.showLong("复制成功")
    }
    //粘贴,注意：AndroidQ 对剪切板的改动是当应用没有获取到焦点时，无法读取剪切板内容。
    fun paste(context: Context): String? {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val cd2 = cm.primaryClip
        if (!cm.hasPrimaryClip()) {
            ToastUtil.showLong("剪切板内容为空")
            return ""
        }
        var s: String? = ""
        try {
            s = cd2?.getItemAt(0)?.text.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return s
    }
}