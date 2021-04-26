package com.smallcake.smallutils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity

object StatusBar {
    private const val TAG = "StatusBar"

    /**
     * 默认透明度
     */
    const val STATUS_BAR_ALPHA = 0

    /**
     * 状态栏id
     */
    private val STATUS_BAR_ID = R.id.status_bar

    /**
     * 显示状态栏
     *
     * @param activity 页面
     */
    fun show(activity: AppCompatActivity?) {
        if (activity == null) {
            return
        }
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 隐藏状态栏
     *
     * @param activity 页面
     */
    fun hide(activity: AppCompatActivity?) {
        if (activity == null) {
            return
        }
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 获取高度
     *
     * @param context 上下文对象
     * @return
     */
    fun height(context: Context): Int {
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    fun immerse(activity: AppCompatActivity) {
        setTransparent(activity)
    }
    //===========================================[状态栏颜色]=============================================
    /**
     * 设置状态栏颜色
     *
     * @param activity 页面
     * @param color    状态栏颜色值
     */
    @JvmStatic
    fun setColor(
        activity: AppCompatActivity,
        @ColorInt color: Int
    ) {
        setColor(activity, color, STATUS_BAR_ALPHA)
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity 页面
     * @param color    颜色值
     * @param alpha    透明度
     */
    fun setColor(
        activity: AppCompatActivity,
        @ColorInt color: Int,
        @IntRange(from = 0, to = 255) alpha: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = createColor(color, alpha)
            setContentTopPadding(
                activity,
                if (color == Color.TRANSPARENT) 0 else height(activity)
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addSameHeightView(activity, color, alpha)
            setFitsSystemWindowsClipPadding(activity)
        }
    }

    /**
     * 设置内容顶部间距
     *
     * @param activity 页面
     * @param padding  间距
     */
    fun setContentTopPadding(
        activity: AppCompatActivity,
        padding: Int
    ) {
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        contentView.setPadding(0, padding, 0, 0)
    }

    /**
     * 重置之前设置
     *
     * @param activity 页面
     */
    fun reset(activity: AppCompatActivity) {
        removeSameHeightView(activity)
        setContentTopPadding(activity, 0)
    }
    //===========================================[文字颜色]=============================================
    /**
     * 设置字体
     *
     * @param activity
     * @param dark     是否是黑色
     */
    @JvmStatic
    fun setTextColor(
        activity: AppCompatActivity,
        dark: Boolean
    ) {
        setMeiZuTextColor(activity, dark)
        setXiaoMiTextColor(activity, dark)
        val visibility =
            if (dark) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN else View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (activity != null) {
            activity.window.decorView.systemUiVisibility = visibility
        }
    }

    /**
     * 修改 MIUI V6  以上状态栏颜色
     */
    private fun setXiaoMiTextColor(
        activity: AppCompatActivity,
        dark: Boolean
    ) {
        val clazz: Class<out Window?> = activity.window.javaClass
        try {
            val layoutParams =
                Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field =
                layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.invoke(activity.window, if (dark) darkModeFlag else 0, darkModeFlag)
        } catch (e: Exception) {
            Log.i(
                TAG,
                "Failed to set the text color of the Xiaomi status bar, the device is not a Xiaomi phone."
            )
        }
    }

    /**
     * 修改魅族状态栏字体颜色 Flyme 4.0
     */
    private fun setMeiZuTextColor(
        activity: AppCompatActivity,
        darkIcon: Boolean
    ) {
        try {
            val lp = activity.window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags =
                WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (darkIcon) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
        } catch (e: Exception) {
            Log.i(
                TAG,
                "Failed to set the text color of Meizu status bar, the device is not a Meizu phone"
            )
        }
    }
    //===========================================[背景透明度|颜色]=============================================
    /**
     * 设置半透明
     *
     * @param activity 页面
     */
    fun setTranslucent(activity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }

    /**
     * 设置全透明
     *
     * @param activity
     */
    fun setTransparent(activity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
     * 设置根布局参数
     */
    fun setFitsSystemWindowsClipPadding(activity: AppCompatActivity) {
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        var i = 0
        val count = contentView.childCount
        while (i < count) {
            val childView = contentView.getChildAt(i)
            if (childView is ViewGroup) {
                val viewGroup = childView
                viewGroup.fitsSystemWindows = true
                viewGroup.clipToPadding = true
            }
            i++
        }
    }
    //===========================================[等高View]=============================================
    /***
     * 设置等高View可见性
     * @param activity 页面
     * @param visibility 可见性
     */
    fun setSameHeightViewVisibility(
        activity: AppCompatActivity,
        visibility: Int
    ) {
        val decorView = activity.window.decorView as ViewGroup
        val statusBarView =
            decorView.findViewById<View>(STATUS_BAR_ID)
        if (statusBarView != null) {
            statusBarView.visibility = visibility
        }
    }

    /**
     * 获取等高View
     *
     * @param activity 页面
     * @return
     */
    fun getSameHeightView(activity: AppCompatActivity): View {
        val decorView = activity.window.decorView as ViewGroup
        return decorView.findViewById(STATUS_BAR_ID)
    }

    /**
     * 添加等高矩形条
     *
     * @param activity 页面
     * @param color    颜色
     * @param alpha    透明值
     */
    fun addSameHeightView(
        activity: AppCompatActivity,
        @ColorInt color: Int,
        @IntRange(from = 0, to = 255) alpha: Int
    ) {
        val decorView = activity.window.decorView as ViewGroup
        val statusBarView =
            decorView.findViewById<View>(STATUS_BAR_ID)
        if (statusBarView != null) {
            if (statusBarView.visibility == View.GONE) {
                statusBarView.visibility = View.VISIBLE
            }
            statusBarView.setBackgroundColor(createColor(color, alpha))
        } else {
            decorView.addView(createSameHeightView(activity, color))
        }
    }

    /**
     * 移除等高View
     *
     * @param activity 页面
     */
    fun removeSameHeightView(activity: AppCompatActivity) {
        val decorView = activity.window.decorView as ViewGroup
        val sameHeightView =
            decorView.findViewById<View>(STATUS_BAR_ID)
        if (sameHeightView != null) {
            decorView.removeView(sameHeightView)
            val rootView =
                (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(
                    0
                ) as ViewGroup
            rootView.setPadding(0, 0, 0, 0)
        }
    }
    /**
     * 创建等高矩形条
     *
     * @param activity 页面
     * @param color    颜色
     * @param alpha    透明值
     * @return
     */
    /**
     * 创建等高矩形条
     *
     * @param activity 页面
     * @param color    颜色
     * @return
     */
    @JvmOverloads
    fun createSameHeightView(
        activity: AppCompatActivity,
        @ColorInt color: Int,
        @IntRange(from = 0, to = 255) alpha: Int = 0
    ): View {
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            height(activity)
        )
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(createColor(color, alpha))
        statusBarView.id = STATUS_BAR_ID
        return statusBarView
    }

    /**
     * 创建颜色
     *
     * @param color color
     * @param alpha 透明值
     * @return 颜色值
     */
    fun createColor(@ColorInt color: Int, alpha: Int): Int {
        if (alpha == 0) {
            return color
        }
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }
}