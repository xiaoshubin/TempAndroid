package com.smallcake.smallutils.text

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.drawable.DrawableCompat
import com.smallcake.smallutils.R

/**
 * 此控件主要是为了方便不写xml的shape
 * 利用代码创建shape,跟shape一样支持
 * 图形类型，填充颜色、边缘线设置，属性名称也一致，
 * 同时为了避免事件的点击冲突采用的是集成自定义的TextView控件。
 * 对比
 * @see com.smallcake.smallutils.ShapeCreator
 * 的动态添加，好处就是可以及时预览
 */
class ShapeTextButton : AppCompatTextView {

    /**
     * 设置状态
     *
     * @param state [.STATE_TEXT] or [.STATE_BUTTON]
     */
    var state = STATE_BUTTON

    /**
     * 获取填充颜色
     *
     * @return
     */
    var solid = Color.LTGRAY //填充颜色
        private set

    /**
     * 获取线宽
     *
     * @return
     */
    var strokeWidth = 0 //线条宽度
        private set

    /**
     * 获取线颜色
     *
     * @return
     */
    var strokeColor = Color.LTGRAY //线条颜色
        private set

    /**
     * 获取图形
     *
     * @return
     */
    var shape = 0 //图形样式
        private set

    /**
     * 获取半径
     *
     * @return
     */
    var radius = 0f //圆角大小
        private set

    /**
     * 获取顶部左边圆角
     *
     * @return
     */
    var topLeftRadius = 0f //左上圆角
        private set

    /**
     * 获取顶部右边圆角
     *
     * @return
     */
    var topRightRadius = 0f //右上圆角
        private set

    /**
     * 获取底部左边圆角
     *
     * @return
     */
    var bottomLeftRadius = 0f //左下圆角
        private set

    /**
     * 获取底部左边圆角
     *
     * @return
     */
    var bottomRightRadius = 0f //右下圆角
        private set

    /**
     * 获取饱和度
     *
     * @return
     */
    var saturation = 0.25f //饱和度，确定按下后的颜色值
        private set
    private var normalDrawable //默认背景
            : Drawable? = null
    private var pressedDrawable //按钮背景
            : Drawable? = null

    constructor(context: Context) : super(context) {
        initAttrs(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextButton)
            solid = typedArray.getColor(R.styleable.ShapeTextButton_stb_solidColor, solid)
            strokeWidth =
                typedArray.getInt(R.styleable.ShapeTextButton_stb_strokeWidth, strokeWidth)
            strokeColor =
                typedArray.getColor(R.styleable.ShapeTextButton_stb_strokeColor, strokeColor)
            if (typedArray.getString(R.styleable.ShapeTextButton_stb_shape) != null) {
                shape = typedArray.getString(R.styleable.ShapeTextButton_stb_shape)!!.toInt()
            }
            state = typedArray.getInt(R.styleable.ShapeTextButton_stb_state, state)
            radius = typedArray.getDimension(R.styleable.ShapeTextButton_stb_radius, 0f)
            topLeftRadius =
                typedArray.getDimension(R.styleable.ShapeTextButton_stb_topLeftRadius, 0f)
            topRightRadius =
                typedArray.getDimension(R.styleable.ShapeTextButton_stb_topRightRadius, 0f)
            bottomLeftRadius =
                typedArray.getDimension(R.styleable.ShapeTextButton_stb_bottomLeftRadius, 0f)
            bottomRightRadius =
                typedArray.getDimension(R.styleable.ShapeTextButton_stb_bottomRightRadius, 0f)
            saturation = typedArray.getFloat(R.styleable.ShapeTextButton_stb_saturation, 0.90f)
            gravity = Gravity.CENTER
            typedArray.recycle()
        }
        drawDrawable()
    }

    /**
     * 绘制Drawable
     */
    protected fun drawDrawable() {
        normalDrawable = createShape(
            shape,
            strokeWidth,
            strokeColor,
            solid,
            radius,
            topLeftRadius,
            topRightRadius,
            bottomLeftRadius,
            bottomRightRadius
        )
        pressedDrawable = createShape(
            shape, strokeWidth, createPressedColor(strokeColor), createPressedColor(
                solid
            ), radius, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius
        )
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(-android.R.attr.state_pressed)
        if (state == STATE_BUTTON) {
            states[1] = intArrayOf(android.R.attr.state_pressed)
        }
        if (state == STATE_TEXT) {
            states[1] = intArrayOf(-android.R.attr.state_pressed)
        }
        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(states[0], normalDrawable)
        if (state == STATE_BUTTON) {
            stateListDrawable.addState(states[1], pressedDrawable)
        }
        if (state == STATE_TEXT) {
            stateListDrawable.addState(states[1], normalDrawable)
        }
        val wrapDrawable = DrawableCompat.wrap(stateListDrawable)
        setDrawable(wrapDrawable)
    }

    /**
     * 使用HSV创建按下状态颜色
     * hsv[2]:值是大的-是深的或亮的
     *
     * @param color
     * @return
     */
    private fun createPressedColor(color: Int): Int {
        val alpha = Color.alpha(color)
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= saturation
        return Color.HSVToColor(alpha, hsv)
    }

    /**
     * 适用于所有android api
     * 设置各种背景。
     *
     * @param drawable
     */
    private fun setDrawable(drawable: Drawable) {
       background = drawable
    }

    /**
     * 创建Shape
     * 这个方法是为了创建一个Shape来替代xml创建Shape.
     *
     * @param shape             类型 GradientDrawable.RECTANGLE  GradientDrawable.OVAL
     * @param strokeWidth       外线宽度 button stroke width
     * @param strokeColor       外线颜色 button stroke color
     * @param solidColor        填充颜色 button background color
     * @param cornerRadius      圆角大小 all corner is the same as is the radius
     * @param topLeftRadius     左上圆角 top left corner radius
     * @param topRightRadius    右上圆角 top right corner radius
     * @param bottomLeftRadius  底左圆角  bottom left corner radius
     * @param bottomRightRadius 底右圆角 bottom right corner radius
     * @return
     */
    fun createShape(
        shape: Int, strokeWidth: Int,
        strokeColor: Int, solidColor: Int, cornerRadius: Float,
        topLeftRadius: Float, topRightRadius: Float,
        bottomLeftRadius: Float, bottomRightRadius: Float
    ): Drawable {
        val drawable = GradientDrawable()
        drawable.shape = shape
        drawable.setSize(10, 10)
        drawable.setStroke(strokeWidth, strokeColor)
        drawable.setColor(solidColor)
        if (cornerRadius != 0f) {
            drawable.cornerRadius = cornerRadius
        } else {
            drawable.cornerRadii = floatArrayOf(
                topLeftRadius,
                topLeftRadius,
                topRightRadius,
                topRightRadius,
                bottomRightRadius,
                bottomRightRadius,
                bottomLeftRadius,
                bottomLeftRadius
            )
        }
        return drawable
    }

    /**
     * 设置填充颜色
     *
     * @param stb_solidColor
     */
    fun setStb_solidColor(stb_solidColor: Int) {
        solid = stb_solidColor
        drawDrawable()
        invalidate()
    }

    /**
     * 设置线宽
     *
     * @param stb_strokeWidth
     */
    fun setStb_strokeWidth(stb_strokeWidth: Int) {
        strokeWidth = stb_strokeWidth
        drawDrawable()
        invalidate()
    }

    /**
     * 设置线颜色
     *
     * @param stb_strokeColor
     */
    fun setStb_strokeColor(stb_strokeColor: Int) {
        strokeColor = stb_strokeColor
        drawDrawable()
        invalidate()
    }

    /**
     * 设置图形
     *
     * @param stb_shape [GradientDrawable.RECTANGLE]  or [GradientDrawable.OVAL]
     */
    fun setStb_shape(stb_shape: Int) {
        shape = stb_shape
        drawDrawable()
        invalidate()
    }

    /**
     * 设置四个方向圆角
     *
     * @param stb_radius
     */
    fun setStb_radius(stb_radius: Float) {
        radius = stb_radius
        drawDrawable()
        invalidate()
    }

    /**
     * 设置顶部左边圆角
     *
     * @param stb_topLeftRadius
     */
    fun setStb_topLeftRadius(stb_topLeftRadius: Float) {
        topLeftRadius = stb_topLeftRadius
        drawDrawable()
        invalidate()
    }

    /**
     * 设置顶部右边圆角
     *
     * @param stb_topRightRadius
     */
    fun setStb_topRightRadius(stb_topRightRadius: Float) {
        topRightRadius = stb_topRightRadius
        drawDrawable()
        invalidate()
    }

    /**
     * 设置底部左边圆角
     *
     * @param stb_bottomLeftRadius
     */
    fun setStb_bottomLeftRadius(stb_bottomLeftRadius: Float) {
        bottomLeftRadius = stb_bottomLeftRadius
        drawDrawable()
        invalidate()
    }

    /**
     * 设置底部右边圆角
     *
     * @param stb_bottomRightRadius
     */
    fun setStb_bottomRightRadius(stb_bottomRightRadius: Float) {
        bottomRightRadius = stb_bottomRightRadius
        drawDrawable()
        invalidate()
    }

    /**
     * 设置饱和度
     *
     * @param stb_saturation
     */
    fun setStb_saturation(stb_saturation: Float) {
        saturation = stb_saturation
        drawDrawable()
        invalidate()
    }

    /**
     * 获取普通状态Drawable
     *
     * @return
     */
    fun getNormalDrawable(): Drawable? {
        return normalDrawable
    }

    /**
     * 设置普通状态Drawable
     *
     * @param normalDrawable
     */
    fun setNormalDrawable(normalDrawable: Drawable?) {
        this.normalDrawable = normalDrawable
        drawDrawable()
        invalidate()
    }

    /**
     * 获取按下状态Drawable
     *
     * @return
     */
    fun getPressedDrawable(): Drawable? {
        return pressedDrawable
    }

    /**
     * 设置按下状态Drawable
     *
     * @param pressedDrawable
     */
    fun setPressedDrawable(pressedDrawable: Drawable?) {
        this.pressedDrawable = pressedDrawable
        drawDrawable()
        invalidate()
    }

    companion object {
        const val STATE_TEXT = 1 //文本状态
        const val STATE_BUTTON = 0 //按钮状态
    }
}