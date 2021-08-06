package com.smallcake.smallutils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View

import android.view.ViewGroup
import android.view.ViewTreeObserver
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

     fun hintKeyboard(activity: Activity) {
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

    /**
     * 监听软键盘的弹出隐藏
     */
    class SoftKeyBoardListener(activity: Activity) {
        private val rootView: View = activity.window.decorView //activity的根视图

        private var rootViewVisibleHeight: Int //纪录根视图的显示高度

        private var onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null
        private fun setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener) {
            this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener
        }

        interface OnSoftKeyBoardChangeListener {
            fun keyBoardShow(height: Int)
            fun keyBoardHide(height: Int)
        }

        companion object {
            fun setListener(
                activity: Activity,
                onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener
            ) {
                val softKeyBoardListener = SoftKeyBoardListener(activity)
                softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener)
            }
        }

        init {
            //获取activity的根视图
            val rl = Rect()
            rootView.getWindowVisibleDisplayFrame(rl)
            rootViewVisibleHeight = rl.height()
            //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
            rootView.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener { //获取当前根视图在屏幕上显示的大小
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)
                val visibleHeight: Int = rect.height()
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight
                    return@OnGlobalLayoutListener
                }

                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return@OnGlobalLayoutListener
                }

                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    if (onSoftKeyBoardChangeListener != null) {
                        onSoftKeyBoardChangeListener!!.keyBoardShow(rootViewVisibleHeight - visibleHeight)
                    }
                    rootViewVisibleHeight = visibleHeight
                    return@OnGlobalLayoutListener
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    if (onSoftKeyBoardChangeListener != null) {
                        onSoftKeyBoardChangeListener!!.keyBoardHide(visibleHeight - rootViewVisibleHeight)
                    }
                    rootViewVisibleHeight = visibleHeight
                    return@OnGlobalLayoutListener
                }
            })
        }
    }
}