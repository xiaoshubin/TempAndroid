package com.smallcake.smallutils.text

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.smallcake.smallutils.R
import com.smallcake.smallutils.StatusBar.setColor
import com.smallcake.smallutils.StatusBar.setTextColor

/**
 * Describe:核心标题栏
 * 使用：
    val bar = NavigationBar(this)
    bar.setLayoutId(R.layout.navigation_bar)//可选
 * 注意:
 * 1.自定义的R.layout.navigation_bar的xml需要包含
 * android:id="@+id/navigation_bar"  FrameLayout
 * android:id="@+id/navigation_bar_back_icon"  ImageView
 * android:id="@+id/navigation_bar_back_text" TextView
 * android:id="@+id/navigation_bar_title" TextView
 * android:id="@+id/navigation_bar_menu_icon" ImageView
 * android:id="@+id/navigation_bar_menu_text" TextView
 * 2.不要在样式中设置
 * <item name="windowNoTitle">true</item> 属性
 *
 */
class NavigationBar(var context: Context) {
    /**
     * 布局Id
     */
    private var layoutId: Int = R.layout.android_navigation_bar
    /**
     * 获取内容View
     */
    private var contentView: View? = null

    /**
     * 动作栏
     *
     */
    private var actionBar: ActionBar? = null
    /**
     * 工具栏
     *
     * @return
     */
    private var toolbar: Toolbar? = null
    /**
     * 自定义的导航栏View
     */
     var navigationView: FrameLayout? = null
    /**
     * 图片返回控件
     */
    var backImageView: ImageView? = null
    /**
     * 文字返回控件
     */
     var backTextView: TextView? = null
    /**
     * 标题控件
     */
     var titleView: TextView? = null
    /**
     * 图片菜单控件
     */
     var menuImageView: ImageView? = null
    /**
     * 文字菜单图片
     */
     var menuTextView: TextView? = null
    /**
     * 布局参数
     */
    private fun onActionBarLayoutParams(): ActionBar.LayoutParams {
        return ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
    }

    /**
     * 创建导航栏
     * @param context  上下文对象
     */
    private fun onLayoutInflater(context: Context) {
        val contentView = LayoutInflater.from(context).inflate(layoutId, null, false)
        if (context is AppCompatActivity) {
            val layoutParams = onActionBarLayoutParams()
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL
            actionBar = context.supportActionBar
            actionBar?.run{
                elevation = 0f
                setDisplayShowHomeEnabled(false)
                setDisplayShowCustomEnabled(true)
                setDisplayShowTitleEnabled(false)
                setCustomView(contentView, layoutParams)
                toolbar = contentView.parent as Toolbar
                toolbar?.setContentInsetsAbsolute(0, 0)
            }

        }
        onCreate(contentView)
    }

    /**
     * 布局创建
     * @param contentView 布局视图
     */
    private fun onCreate(contentView: View) {
        this.contentView = contentView
        navigationView = contentView.findViewById(R.id.navigation_bar)
        backImageView = contentView.findViewById(R.id.navigation_bar_back_icon)
        backTextView = contentView.findViewById(R.id.navigation_bar_back_text)
        titleView = contentView.findViewById(R.id.navigation_bar_title)
        menuImageView = contentView.findViewById(R.id.navigation_bar_menu_icon)
        menuTextView = contentView.findViewById(R.id.navigation_bar_menu_text)

    }

    /**
     * 设置自定义资源id
     *
     * @param layoutId
     */
    fun setLayoutId(layoutId: Int) {
        this.layoutId = layoutId
        onLayoutInflater(context)
    }

    /**
     * 隐藏
     */
    fun hide() {
        if (actionBar != null && actionBar!!.isShowing) {
            actionBar?.hide()
        }
        if (contentView!=null&&contentView?.parent!=null){
            val parent = contentView?.parent as ViewGroup
            parent.visibility = View.GONE
        }

    }

    /**
     * 是否显示
     *
     * @return
     */
    val isShowing: Boolean
        get() = if (actionBar == null) {
            false
        } else actionBar!!.isShowing

    /**
     * 显示
     */
    fun show() {
        if (actionBar != null && !actionBar?.isShowing!!) {
            actionBar!!.show()
        }
        if (contentView != null) {
            val parent = contentView!!.parent as ViewGroup
            parent.visibility = View.VISIBLE
        }
    }

    /**
     * 设置侵入式
     * @param statusBarTextDark 状态栏文字颜色
     */
    fun setImmersed(statusBarTextDark: Boolean) {
        hide()
        setBackgroundColor(Color.TRANSPARENT)
        setStatusBarTextColor(statusBarTextDark)
    }

    /**
     * 设置背景颜色
     * @param color 颜色
     */
    private fun setBackgroundColor(@ColorInt color: Int) {
        setBackgroundColor(color, true)
    }

    /**
     * 设置背景颜色
     *
     * @param color          颜色
     * @param applyStatusBar 状态栏是否应用
     */
    fun setBackgroundColor( @ColorInt color: Int,applyStatusBar: Boolean) {
        navigationView?.setBackgroundColor(color)
        if (context is AppCompatActivity) {
            val activity = context as AppCompatActivity
            setStatusBarTextColor(color == Color.WHITE || color == Color.TRANSPARENT)
            if (applyStatusBar) setColor(activity, color)
        }
    }

    /**
     * 设置状态栏文字颜色
     *
     * @param dark 是否黑色
     */
    private fun setStatusBarTextColor(dark: Boolean) {
        if (context is AppCompatActivity) {
            val activity =context as AppCompatActivity
            setTextColor(activity, dark)
        }
    }

    /**
     * 设置背景颜色
     *
     * @param resId
     */
    fun setBackgroundResource(@ColorRes resId: Int) {
        val color = context.resources.getColor(resId)
        setBackgroundColor(color)
    }

    /**
     * 设置背景颜色
     *
     * @param resId          颜色
     * @param applyStatusBar 状态栏是否应用
     */
    fun setBackgroundResource(@ColorRes resId: Int, applyStatusBar: Boolean) {
        val color = context.resources.getColor(resId)
        setBackgroundColor(color, applyStatusBar)
    }

    /**
     * 设置标题
     * @param text
     */
    fun setTitle(text: CharSequence?) {
        titleView?.text = text
    }

    /**
     * 构建导航栏
     * @param context    上下文
     */
    init {
        onLayoutInflater(context)
    }

}