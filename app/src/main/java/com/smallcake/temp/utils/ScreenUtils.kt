package com.smallcake.temp.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.View.MeasureSpec
import android.view.WindowManager
import androidx.annotation.FloatRange
import com.smallcake.temp.MyApplication


/**
 * 屏幕工具
 */
object ScreenUtils {
    /**
     * 获取屏幕实际高度（也包含虚拟导航栏）
     * @return
     */
    fun getHeight(): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager = MyApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
         windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
    fun getWidth(): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager = MyApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
         windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    /**
     * 获取电量栏高度
     *
     * @return
     */
    fun getStatusHeight(): Int {
        var result = 0
        val resourceId: Int =MyApplication.instance.getResources()
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = MyApplication.instance.getResources().getDimensionPixelSize(resourceId)
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
        val width: Int = getWidth()
        val height: Int = getHeight()
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
}