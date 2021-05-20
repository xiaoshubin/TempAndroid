package com.smallcake.smallutils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View

import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

/**
 * 软键盘工具类
 */
object KeyboardUtils {
    /**
     * 设置页面中要被顶起的视图View
     * 注意：页面根布局需要设置android:fitsSystemWindows="true"
     * 列表需要滚动到末尾
     * 例如：
    bind.etMsg.setOnFocusChangeListener{
            _,hasFocus ->if (hasFocus){
                Handler().postDelayed({ bind.recyclerView.smoothScrollToPosition(mAdapter.data.size)},300)
            }
        }
     */
    @SuppressLint("ClickableViewAccessibility")
    fun setHintKeyboardView(activity: Activity,view: View ) {
        view.setOnTouchListener { _, _ ->
            hintKeyboard(activity)
            false
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) setHintKeyboardView(activity, view.getChildAt(i))
        }
    }

    private fun hintKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && activity.currentFocus != null) {
            if (activity.currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }
}