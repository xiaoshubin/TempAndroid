package com.smallcake.smallutils

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat

/**
 * 解决shape.xml文件过多，动态设置圆角样式
 * //圆角
ShapeCreator.create()
.setCornerRadius(10)
.setSolidColor(Color.GRAY)
.into(btn1);
 * //带交互效果状态
ShapeCreator.create()
.setCornerRadius(10)
.setStateEnabled(true)
.setSolidColor(Color.GRAY)
.setSolidPressColor(Color.DKGRAY)
.setStrokeColor(Color.CYAN)
.setStrokePressColor(Color.MAGENTA)
.setStrokeWidth(2)
.setStateTextColorEnabled(true)
.setTextColor(Color.BLACK)
.setTextPressColor(Color.WHITE)
.into(btn18)
 */
class ShapeCreator {
    /**
     * 对应标签 shape
     */
    private var shape = 0

    /**
     * 对应标签 corners
     */
    private var cornerRadius = 0f  //四个角的半径 = 0f
    private var cornerRadiusLT = 0f //左上角半径 = 0f
    private var cornerRadiusRT = 0f //右上角半径 = 0f
    private var cornerRadiusLB  = 0f //左下角半径 = 0f
    private var cornerRadiusRB = 0f //右下角半径 = 0f

    /**
     * 对应标签 solid
     */
    private var solidColor = 0
    private var solidPressColor = 0
    private var solidDisableColor = 0

    /**
     * 对应标签stroke
     */
    private var strokeColor = 0
    private var strokePressColor = 0
    private var strokeDisableColor = 0
    private var strokeWidth = 0
    private var strokeDashWidth = 0
    private var strokeDashGap = 0

    /**
     * 对应标签gradient
     */
    private var gradientType = -1
    private var gradientAngle = 0
    private var gradientCenterX = 0f
    private var gradientCenterY = 0f
    private var gradientStartColor = 0
    private var gradientCenterColor = 0
    private var gradientEndColor = 0
    private var gradientRadius = 0f

    /**
     * 对应标签 size
     */
    private var width = 0
    private var height = 0

    /**
     * 对应标签 padding
     */
    private val padding = intArrayOf(0, 0, 0, 0)

    /**
     * true 默认dp
     * false 默认px
     */
    private var dpUnitEnabled = true

    /**
     * 状态标签是否启用  selector 标签
     */
    private var stateEnabled = false

    /**
     * 是否支持设置文字颜色state
     */
    private var stateTextColorEnabled = false
    private var textColor = 0
    private var textPressColor = 0
    private var textDisableColor = 0

    /**
     * <corners android:radius=""></corners>
     * @param radius
     */
    fun setCornerRadius(radius: Float): ShapeCreator {
        cornerRadius = radius
        return this
    }

    /**
     * <corners android:topLeftRadius=""></corners>
     *
     * @param cornerRadiusLT
     */
    fun setCornerRadiusLT(cornerRadiusLT: Float): ShapeCreator {
        this.cornerRadiusLT = cornerRadiusLT
        return this
    }

    /**
     * <corners android:topRightRadius=""></corners>
     *
     * @param cornerRadiusRT
     */
    fun setCornerRadiusRT(cornerRadiusRT: Float): ShapeCreator {
        this.cornerRadiusRT = cornerRadiusRT
        return this
    }

    /**
     * <corners android:bottomLeftRadius=""></corners>
     *
     * @param cornerRadiusLB
     */
    fun setCornerRadiusLB(cornerRadiusLB: Float): ShapeCreator {
        this.cornerRadiusLB = cornerRadiusLB
        return this
    }

    /**
     * <corners android:bottomRightRadius=""></corners>
     *
     * @param cornerRadiusRB
     */
    fun setCornerRadiusRB(cornerRadiusRB: Float): ShapeCreator {
        this.cornerRadiusRB = cornerRadiusRB
        return this
    }

    /**
     * <solid android:color=""></solid>
     *
     * @param solidColor
     */
    fun setSolidColor(solidColor: Int): ShapeCreator {
        this.solidColor = solidColor
        return this
    }

    /**
     * state_press='true' color
     *
     * @param solidPressColor
     */
    fun setSolidPressColor(solidPressColor: Int): ShapeCreator {
        this.solidPressColor = solidPressColor
        return this
    }

    /**
     * state_enabled='false' color
     *
     * @param solidDisableColor
     */
    fun setSolidDisableColor(solidDisableColor: Int): ShapeCreator {
        this.solidDisableColor = solidDisableColor
        return this
    }

    /**
     * <stroke android:color=""></stroke>
     *
     * @param strokeColor
     */
    fun setStrokeColor(strokeColor: Int): ShapeCreator {
        this.strokeColor = strokeColor
        return this
    }

    /**
     * state_press='true' color
     *
     * @param strokePressColor
     */
    fun setStrokePressColor(strokePressColor: Int): ShapeCreator {
        this.strokePressColor = strokePressColor
        return this
    }

    /**
     * state_enabled='false' color
     *
     * @param strokeDisableColor
     */
    fun setStrokeDisableColor(strokeDisableColor: Int): ShapeCreator {
        this.strokeDisableColor = strokeDisableColor
        return this
    }

    /**
     * <stroke android:width=""></stroke>
     *
     * @param strokeWidth
     */
    fun setStrokeWidth(strokeWidth: Int): ShapeCreator {
        this.strokeWidth = strokeWidth
        return this
    }

    /**
     * <stroke android:dashWidth=""></stroke>
     *
     * @param strokeDashWidth
     */
    fun setStrokeDashWidth(strokeDashWidth: Int): ShapeCreator {
        this.strokeDashWidth = strokeDashWidth
        return this
    }

    /**
     * <stroke android:dashGap=""></stroke>
     *
     * @param strokeDashGap
     */
    fun setStrokeDashGap(strokeDashGap: Int): ShapeCreator {
        this.strokeDashGap = strokeDashGap
        return this
    }

    /**
     * <shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="">
     *
     * @param shape
    </shape> */
    fun setShape(shape: Int): ShapeCreator {
        this.shape = shape
        return this
    }

    /**
     * <gradient android:type=""></gradient>
     *
     * @param gradientType
     */
    fun setGradientType(gradientType: Int): ShapeCreator {
        this.gradientType = gradientType
        return this
    }

    /**
     * <gradient android:angle=""></gradient>
     *
     * @param gradientAngle
     */
    fun setGradientAngle(gradientAngle: Int): ShapeCreator {
        this.gradientAngle = gradientAngle
        defaultGradientType()
        return this
    }

    /**
     * <gradient android:centerX=""></gradient>
     *
     * @param gradientCenterX
     */
    fun setGradientCenterX(gradientCenterX: Float): ShapeCreator {
        this.gradientCenterX = gradientCenterX
        defaultGradientType()
        return this
    }

    /**
     * <gradient android:centerY=""></gradient>
     *
     * @param gradientCenterY
     */
    fun setGradientCenterY(gradientCenterY: Float): ShapeCreator {
        this.gradientCenterY = gradientCenterY
        defaultGradientType()
        return this
    }

    /**
     * <gradient android:startColor=""></gradient>
     *
     * @param gradientStartColor
     */
    fun setGradientStartColor(gradientStartColor: Int): ShapeCreator {
        this.gradientStartColor = gradientStartColor
        defaultGradientType()
        return this
    }

    /**
     * <gradient android:centerColor=""></gradient>
     *
     * @param gradientCenterColor
     */
    fun setGradientCenterColor(gradientCenterColor: Int): ShapeCreator {
        this.gradientCenterColor = gradientCenterColor
        defaultGradientType()
        return this
    }

    /**
     * * <gradient android:endColor=""></gradient>
     *
     * @param gradientEndColor
     */
    fun setGradientEndColor(gradientEndColor: Int): ShapeCreator {
        this.gradientEndColor = gradientEndColor
        defaultGradientType()
        return this
    }

    /**
     * * <gradient android:gradientRadius=""></gradient>
     *
     * @param gradientRadius
     */
    fun setGradientRadius(gradientRadius: Float): ShapeCreator {
        this.gradientRadius = gradientRadius
        defaultGradientType()
        return this
    }

    /**
     * <size android:width="" android:height=""></size>
     *
     * @param w
     * @param h
     */
    fun setSize(w: Int, h: Int): ShapeCreator {
        width = w
        height = h
        return this
    }

    /**
     * <padding>
     *
     * @param padding
    </padding> */
    fun setPadding(padding: Int): ShapeCreator {
        for (i in this.padding.indices) {
            this.padding[i] = padding
        }
        return this
    }

    /**
     * <padding left="">
     *
     * @param padding
    </padding> */
    fun setPaddingLeft(padding: Int): ShapeCreator {
        this.padding[0] = padding
        return this
    }

    /**
     * <padding top="">
     *
     * @param padding
    </padding> */
    fun setPaddingTop(padding: Int): ShapeCreator {
        this.padding[1] = padding
        return this
    }

    /**
     * <padding right="">
     *
     * @param padding
    </padding> */
    fun setPaddingRight(padding: Int): ShapeCreator {
        this.padding[2] = padding
        return this
    }

    /**
     * <padding bottom="">
     *
     * @param padding
    </padding> */
    fun setPaddingBottom(padding: Int): ShapeCreator {
        this.padding[3] = padding
        return this
    }

    /**
     * 是否dp转px
     *
     * @param dpUnitEnabled true dp false px
     */
    fun setDpUnitEnabled(dpUnitEnabled: Boolean): ShapeCreator {
        this.dpUnitEnabled = dpUnitEnabled
        return this
    }

    /**
     * 是否开启交互状态
     *
     * @param stateEnabled true 支持 state_press="true' state_enabled="false"
     */
    fun setStateEnabled(stateEnabled: Boolean): ShapeCreator {
        this.stateEnabled = stateEnabled
        return this
    }

    /**
     * 文字颜色交互状态
     *
     * @param stateTextColorEnabled
     */
    fun setStateTextColorEnabled(stateTextColorEnabled: Boolean): ShapeCreator {
        this.stateTextColorEnabled = stateTextColorEnabled
        return this
    }

    /**
     * 默认颜色
     *
     * @param textColor
     */
    fun setTextColor(textColor: Int): ShapeCreator {
        this.textColor = textColor
        return this
    }

    /**
     * 按下颜色
     *
     * @param textPressColor
     */
    fun setTextPressColor(textPressColor: Int): ShapeCreator {
        this.textPressColor = textPressColor
        return this
    }

    /**
     * 禁用颜色
     *
     * @param textDisableColor
     */
    fun setTextDisableColor(textDisableColor: Int): ShapeCreator {
        this.textDisableColor = textDisableColor
        return this
    }

    fun into(view: View) {
        val context = view.context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val drawable = createGradientDrawable(context, -1)
            drawable.useLevel = false
            ViewCompat.setBackground(view, drawable)
        } else {
            if (!stateEnabled) {
                val drawable = createGradientDrawable(context, -1)
                drawable.useLevel = false
                ViewCompat.setBackground(view, drawable)
            } else {
                val drawable =
                    StateListDrawable()
                val press = createGradientDrawable(context, 1)
                press.useLevel = false
                val disable = createGradientDrawable(context, 2)
                disable.useLevel = false
                val normal = createGradientDrawable(context, 3)
                normal.useLevel = false
                drawable.addState(intArrayOf(R.attr.state_pressed), press)
                drawable.addState(intArrayOf(-R.attr.state_enabled), disable)
                drawable.addState(intArrayOf(), normal)
                ViewCompat.setBackground(view, drawable)
            }
        }
        if (stateTextColorEnabled && view is TextView) {
            val state = arrayOfNulls<IntArray>(3)
            state[0] = intArrayOf(R.attr.state_pressed)
            state[1] = intArrayOf(-R.attr.state_enabled)
            state[2] = intArrayOf()
            if (textColor != 0) {
                if (textPressColor == 0) {
                    textPressColor = textColor
                }
                if (textDisableColor == 0) {
                    textDisableColor = textColor
                }
                val colorStateList =
                    ColorStateList(state, intArrayOf(textPressColor, textDisableColor, textColor))
                view.setTextColor(colorStateList)
            }
        }
    }

    private fun createGradientDrawable(
        context: Context,
        type: Int
    ): GradientDrawable {
        val drawable = GradientDrawable()
        setXmlShapeType(drawable)
        setXmlSolid(drawable, type)
        setXmlSize(drawable, context)
        setXmlPadding(drawable, context)
        setXmlCorners(drawable, context)
        setXmlStroke(drawable, context, type)
        setXmlGradient(drawable, context)
        return drawable
    }

    /**
     * <shape android:shape='xxxx'>
     *
     * @param drawable
    </shape> */
    private fun setXmlShapeType(drawable: GradientDrawable) {
        drawable.shape = shape
    }

    /**
     * <corners></corners>
     *
     * @param drawable
     * @param context
     */
     fun setXmlCorners(
        drawable: GradientDrawable,
        context: Context
    ) {
        if (shape == RECTANGLE) {
            //矩形可以设置圆角
            if (cornerRadius != 0f) {
                drawable.cornerRadius = dip2px(context, cornerRadius).toFloat()
            } else {
                drawable.cornerRadii = floatArrayOf( //左上
                    dip2px(context, cornerRadiusLT).toFloat(),
                    dip2px(context, cornerRadiusLT).toFloat(),  //右上
                    dip2px(context, cornerRadiusRT).toFloat(),
                    dip2px(context, cornerRadiusRT).toFloat(),  //右下
                    dip2px(context, cornerRadiusRB).toFloat(),
                    dip2px(context, cornerRadiusRB).toFloat(),  //左下
                    dip2px(context, cornerRadiusLB).toFloat(),
                    dip2px(context, cornerRadiusLB)
                        .toFloat()
                )
            }
        }
    }

    /**
     * <solid></solid>
     *
     * @param drawable
     * @param type     当stateEnabled=true,同时Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP 生效
     * 1:state_pressed=true
     * 2:state_enabled=false
     * 3:默认状态
     */
    private fun setXmlSolid(drawable: GradientDrawable, type: Int) {
        if (!stateEnabled) {
            drawable.setColor(solidColor)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val state = arrayOfNulls<IntArray>(3)
                state[0] = intArrayOf(R.attr.state_pressed)
                state[1] = intArrayOf(-R.attr.state_enabled)
                state[2] = intArrayOf()
                val colorStateList = ColorStateList(
                    state,
                    intArrayOf(solidPressColor, solidDisableColor, solidColor)
                )
                drawable.color = colorStateList
            } else {
                when (type) {
                    1 -> drawable.setColor(solidPressColor)
                    2 -> drawable.setColor(solidDisableColor)
                    3 -> drawable.setColor(solidColor)
                }
            }
        }
    }

    /**
     * <stroke></stroke>
     *
     * @param drawable
     * @param context
     * @param type     当stateEnabled=true,同时Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP 生效
     * 1:state_pressed=true
     * 2:state_enabled=false
     * 3:默认状态
     */
    private fun setXmlStroke(
        drawable: GradientDrawable,
        context: Context,
        type: Int
    ) {
        if (!stateEnabled) {
            drawable.setStroke(
                dip2px(context, strokeWidth.toFloat()),
                strokeColor,
                dip2px(context, strokeDashWidth.toFloat()).toFloat(),
                dip2px(context, strokeDashGap.toFloat()).toFloat()
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val state = arrayOfNulls<IntArray>(3)
                state[0] = intArrayOf(R.attr.state_pressed)
                state[1] = intArrayOf(-R.attr.state_enabled)
                state[2] = intArrayOf()
                val colorStateList = ColorStateList(
                    state,
                    intArrayOf(strokePressColor, strokeDisableColor, strokeColor)
                )
                drawable.setStroke(
                    dip2px(context, strokeWidth.toFloat()),
                    colorStateList,
                    dip2px(context, strokeDashWidth.toFloat()).toFloat(),
                    dip2px(context, strokeDashGap.toFloat()).toFloat()
                )
            } else {
                var color = solidColor
                when (type) {
                    1 -> color = strokePressColor
                    2 -> color = strokeDisableColor
                    3 -> color = strokeColor
                }
                drawable.setStroke(
                    dip2px(context, strokeWidth.toFloat()),
                    color,
                    dip2px(context, strokeDashWidth.toFloat()).toFloat(),
                    dip2px(context, strokeDashGap.toFloat()).toFloat()
                )
            }
        }
    }

    /**
     * <size></size>
     *
     * @param drawable
     * @param context
     */
    private fun setXmlSize(
        drawable: GradientDrawable,
        context: Context
    ) {
        if (width > 0 && height > 0) {
            drawable.setSize(dip2px(context, width.toFloat()), dip2px(context, height.toFloat()))
        }
    }

    /**
     * <padding></padding>
     *
     * @param drawable
     * @param context
     */
    private fun setXmlPadding(
        drawable: GradientDrawable,
        context: Context
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setPadding(
                dip2px(context, padding[0].toFloat()),
                dip2px(context, padding[1].toFloat()),
                dip2px(context, padding[2].toFloat()),
                dip2px(context, padding[3].toFloat())
            )
        }
    }

    /**
     * <gradient></gradient>
     *
     * @param drawable
     * @param context
     */
    private fun setXmlGradient(
        drawable: GradientDrawable,
        context: Context
    ) {
        if (gradientType == -1) return
        if (gradientType == LINEAR_GRADIENT) {
            drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        } else if (gradientType == RADIAL_GRADIENT) {
            drawable.gradientType = GradientDrawable.RADIAL_GRADIENT
        } else if (gradientType == SWEEP_GRADIENT) {
            drawable.gradientType = GradientDrawable.SWEEP_GRADIENT
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (gradientCenterColor == 0) {
                drawable.colors = intArrayOf(gradientStartColor, gradientEndColor)
            } else {
                drawable.colors = intArrayOf(
                    gradientStartColor,
                    gradientCenterColor,
                    gradientEndColor
                )
            }
            var mOrientation =
                GradientDrawable.Orientation.TOP_BOTTOM
            when (gradientAngle) {
                0 -> mOrientation = GradientDrawable.Orientation.LEFT_RIGHT
                45 -> mOrientation = GradientDrawable.Orientation.BL_TR
                90 -> mOrientation = GradientDrawable.Orientation.BOTTOM_TOP
                135 -> mOrientation = GradientDrawable.Orientation.BR_TL
                180 -> mOrientation = GradientDrawable.Orientation.RIGHT_LEFT
                225 -> mOrientation = GradientDrawable.Orientation.TR_BL
                270 -> mOrientation = GradientDrawable.Orientation.TOP_BOTTOM
                315 -> mOrientation = GradientDrawable.Orientation.TL_BR
            }
            drawable.orientation = mOrientation
        } else {
            drawable.setColor(solidColor)
        }
        drawable.setGradientCenter(gradientCenterX, gradientCenterY)
        drawable.gradientRadius = dip2px(context, gradientRadius).toFloat()
    }

    /**
     * 设置默认渐变类型
     */
    private fun defaultGradientType() {
        if (gradientType == -1) {
            gradientType = LINEAR_GRADIENT
        }
    }

    private fun dip2px(context: Context, dipValue: Float): Int {
        return if (dpUnitEnabled) {
            val scale = context.resources.displayMetrics.density
            (dipValue * scale + 0.5f).toInt()
        } else {
            dipValue.toInt()
        }
    }

    companion object {
        const val RECTANGLE = 0
        const val OVAL = 1
        const val LINE = 2
        const val LINEAR_GRADIENT = 0
        const val RADIAL_GRADIENT = 1
        const val SWEEP_GRADIENT = 2
        fun create(): ShapeCreator {
            return ShapeCreator()
        }

        /**
         * 直接指定类型 shape line
         *
         */
        fun line(): ShapeCreator {
            return ShapeCreator().setShape(LINE)
        }

        /**
         * 直接指定类型 shape oval
         */
        fun oval(): ShapeCreator {
            return ShapeCreator().setShape(OVAL)
        }
    }
}