package com.smallcake.smallutils

import android.app.AlertDialog
import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Date:2021/6/2 17:08
 * Author:SmallCake
 * Desc:Snackbar的显示
 **/
object SnackbarUtils {
    /**
     * 显示信息
     */
    fun show(v:View,msg:CharSequence){
        Snackbar.make(v,msg, Snackbar.LENGTH_LONG).show()
    }

    /**
     * 点击后查看更多信息
     */
    fun showMoreClick(v:View,msg:CharSequence){
        Snackbar.make(v,msg, Snackbar.LENGTH_LONG)
            .setAction("查看更多") {
                AlertDialog.Builder(it.context)
                    .setMessage(msg)
                    .setPositiveButton("知道了",null)
                    .setNegativeButton("",null)
                    .show()
            }
            .show()
    }

}