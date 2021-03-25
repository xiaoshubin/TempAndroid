package com.smallcake.smallutils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.*

/**
 * Date: 2020/3/17
 * author: SmallCake
 * 属性动画工具类
 */
object AnimUtils {
    /**
     * 点击选中效果
     * @param view View
     */
    fun tabSelect(view: View) {
        val alpha = PropertyValuesHolder.ofFloat("alpha", 0.6f, 1f)
        val pvhX = PropertyValuesHolder.ofFloat("scaleX", 0.9f, 1.1f, 1f)
        val pvhY = PropertyValuesHolder.ofFloat("scaleY", 0.9f, 1.1f, 1f)
        val animator =
            ObjectAnimator.ofPropertyValuesHolder(view, alpha, pvhX, pvhY)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 300
        animator.start()
    }

    /**
     * 浮动动画
     * @param view
     */
    fun floatView(view: View) {
        val alpha = PropertyValuesHolder.ofFloat("alpha", 0.6f, 1f)
        val translateY = PropertyValuesHolder.ofFloat("translationY", 8f)
        val animator =
            ObjectAnimator.ofPropertyValuesHolder(view, alpha, translateY)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.duration = 1000
        animator.start()
    }

    /**
     * 放大
     * @param view View
     */
    fun scaleView(view: View) {
        val alpha = PropertyValuesHolder.ofFloat("alpha", 0.8f, 1f)
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.9f, 1.1f)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.9f, 1.1f)
        val animator =
            ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.duration = 300
        animator.start()
    }

    /**
     * 阳光普照
     * @param view View
     */
    fun sunshineRotate(view: View) {
        val anim: Animation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        anim.fillAfter = true // 设置保持动画最后的状态
        anim.duration = 5000 // 设置动画时间
        anim.repeatCount = ValueAnimator.INFINITE
        anim.repeatMode = ValueAnimator.RESTART
        anim.interpolator = LinearInterpolator()
        view.startAnimation(anim)
    }

    /**
     * 旋转
     * @param view View
     * @return ObjectAnimator
     */
    fun rotateAnim(view: View): ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(view, "rotationY", 0f, 360f)
        animator.repeatCount = Animation.INFINITE
        animator.repeatMode = ObjectAnimator.RESTART
        animator.interpolator = OvershootInterpolator()
        animator.duration = 1000
        animator.start()
        return animator
    }
}