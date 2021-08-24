package com.smallcake.smallutils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewConfiguration
import android.view.WindowManager
import androidx.annotation.FloatRange


/**
 * 屏幕工具
 */
object Screen {

    /**
     * 获取屏幕实际高度（也包含虚拟导航栏）
     * @return
     */
    val height: Int
        get(){
            val displayMetrics = DisplayMetrics()
            val windowManager = SmallUtils.context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getRealMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }
    /**
     * 获取屏幕实际宽度
     * @return
     */
    val width:Int
        get(){
            val displayMetrics = DisplayMetrics()
            val windowManager = SmallUtils.context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getRealMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

    /**
     * 获取电量栏高度
     *
     * @return
     */
    val statusHeight: Int
    get(){
        var result = 0
        val resourceId: Int =SmallUtils.context?.resources?.getIdentifier("status_bar_height", "dimen", "android")?:24
        if (resourceId > 0) {
            result = SmallUtils.context?.resources?.getDimensionPixelSize(resourceId)?:0
        }
        return result
    }

    /**
     * 屏幕截图
     * @param activity
     * @param hasStatus 是否包含状态栏，默认：是
     * @return bitmap
     * 注意：禁止某些页面截图，可以设置安全标记
     *  window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
     */
    fun screenShot(activity: Activity,hasStatus:Boolean = true): Bitmap? {
        val view = activity.window.decorView
        val width: Int = width
        val height: Int = height
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache ?: return null
        var bp: Bitmap = if (!hasStatus){
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            val statusBarHeight = frame.top
            Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        }else Bitmap.createBitmap(bmp, 0, 0, width, height)
        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()
        return bp
    }

    /**
     * 截取控件视图
     * @param view View
     * @return Bitmap?
     */
    fun screenShotView(view: View): Bitmap? {
        view.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.isDrawingCacheEnabled = true
        val b = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        view.buildDrawingCache()
        return  b
    }

    /**
     * 设置屏幕透明度
     * @param context
     * @param alpha
     */
    fun setWindowBackgroundAlpha(context: Activity,@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        val lp = context.window.attributes
        lp.alpha = alpha //0.0-1.0
        context.window.attributes = lp
    }
    /**
     * 获取底部菜单栏高度
     */
    val navigationBarHeight: Int
        get() {
            var result = 0
            if (hasNavBar()) {
                val res: Resources = SmallUtils.context!!.resources
                val resourceId: Int = res.getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    result = res.getDimensionPixelSize(resourceId)
                }
            }
            return result
        }


    /**
     * 检查是否存在虚拟按键栏
     *
     * @return
     */
    private fun hasNavBar(): Boolean {
        val res = SmallUtils.context!!.resources
        val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
        return if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId)
            // check override flag
            val sNavBarOverride: String? = getNavBarOverride()
            if ("1" == sNavBarOverride) {
                hasNav = false
            } else if ("0" == sNavBarOverride) {
                hasNav = true
            }
            hasNav
        } else { // fallback
            !ViewConfiguration.get(SmallUtils.context!!).hasPermanentMenuKey()
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     */
    @SuppressLint("PrivateApi")
    private fun getNavBarOverride(): String? {
        var sNavBarOverride: String? = null
        try {
            val c = Class.forName("android.os.SystemProperties")
            val m = c.getDeclaredMethod("get", String::class.java)
            m.isAccessible = true
            sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
        } catch (e: Throwable) {
        }

        return sNavBarOverride
    }
}