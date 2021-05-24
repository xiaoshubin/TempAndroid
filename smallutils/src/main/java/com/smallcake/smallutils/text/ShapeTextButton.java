package com.smallcake.smallutils.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.smallcake.smallutils.R;


/**
 * 此控件主要是为了方便不写xml的shape
 * 利用代码创建shape,跟shape一样支持
 * 图形类型，填充颜色、边缘线设置，属性名称也一致，
 * 同时为了避免事件的点击冲突采用的是集成自定义的TextView控件。
 * 对比
 * @see com.smallcake.smallutils.ShapeCreator
 * 的动态添加，好处就是可以及时预览
 */
public class ShapeTextButton extends AppCompatTextView {

    /**
     * 文本状态
     */
    public static final int STATE_TEXT = 1;
    /**
     * 按钮状态
     */
    public static final int STATE_BUTTON = 0;
    /**
     * 填充颜色
     */
    private int solid = Color.LTGRAY;
    /**
     * 线条宽度
     */
    private int strokeWidth = 0;
    /**
     * 线条颜色
     */
    private int strokeColor = Color.LTGRAY;
    /**
     * 图形
     */
    private int shape = 0;
    /**
     * 状态
     */
    private int state = STATE_BUTTON;
    /**
     * 圆角
     */
    private float radius = 0;
    /**
     * 左上圆角
     */
    private float topLeftRadius = 0;
    /**
     * 右上圆角
     */
    private float topRightRadius = 0;
    /**
     * 左下圆角
     */
    private float bottomLeftRadius = 0;
    /**
     * 右下圆角
     */
    private float bottomRightRadius = 0;
    /**
     * 饱和度
     */
    private float saturation = 0.25f;
    /**
     * 默认背景
     */
    private Drawable normalDrawable;
    /**
     * 按钮背景
     */
    private Drawable pressedDrawable;


    public ShapeTextButton(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public ShapeTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public ShapeTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextButton);
            solid = typedArray.getColor(R.styleable.ShapeTextButton_stb_solidColor, solid);
            strokeWidth = typedArray.getInt(R.styleable.ShapeTextButton_stb_strokeWidth, strokeWidth);
            strokeColor = typedArray.getColor(R.styleable.ShapeTextButton_stb_strokeColor, strokeColor);
            if (typedArray.getString(R.styleable.ShapeTextButton_stb_shape) != null) {
                shape = Integer.parseInt(typedArray.getString(R.styleable.ShapeTextButton_stb_shape));
            }
            state = typedArray.getInt(R.styleable.ShapeTextButton_stb_state, state);
            radius = typedArray.getDimension(R.styleable.ShapeTextButton_stb_radius, 0);
            topLeftRadius = typedArray.getDimension(R.styleable.ShapeTextButton_stb_topLeftRadius, 0);
            topRightRadius = typedArray.getDimension(R.styleable.ShapeTextButton_stb_topRightRadius, 0);
            bottomLeftRadius = typedArray.getDimension(R.styleable.ShapeTextButton_stb_bottomLeftRadius, 0);
            bottomRightRadius = typedArray.getDimension(R.styleable.ShapeTextButton_stb_bottomRightRadius, 0);
            saturation = typedArray.getFloat(R.styleable.ShapeTextButton_stb_saturation, 0.90f);
            typedArray.recycle();
        }
        drawDrawable();
    }

    /**
     * 绘制Drawable
     */
    protected void drawDrawable() {
        normalDrawable = createShape(shape, strokeWidth, strokeColor, solid, radius, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        pressedDrawable = createShape(shape, strokeWidth, createPressedColor(strokeColor), createPressedColor(solid), radius, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        int[][] states = new int[2][];
        states[0] = new int[]{-android.R.attr.state_pressed};
        if (state == STATE_BUTTON) {
            states[1] = new int[]{android.R.attr.state_pressed};
        }
        if (state == STATE_TEXT) {
            states[1] = new int[]{-android.R.attr.state_pressed};
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(states[0], normalDrawable);
        if (state == STATE_BUTTON) {
            stateListDrawable.addState(states[1], pressedDrawable);
        }
        if (state == STATE_TEXT) {
            stateListDrawable.addState(states[1], normalDrawable);
        }
        Drawable wrapDrawable = DrawableCompat.wrap(stateListDrawable);
        setDrawable(wrapDrawable);
    }


    /**
     * 使用HSV创建按下状态颜色
     * hsv[2]:值是大的-是深的或亮的
     *
     * @param color
     * @return
     */
    private int createPressedColor(int color) {
        int alpha = Color.alpha(color);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= saturation;
        return Color.HSVToColor(alpha, hsv);
    }


    /**
     * 适用于所有android api
     * 设置各种背景。
     *
     * @param drawable
     */
    private void setDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
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
    public Drawable createShape(int shape, int strokeWidth,
                                int strokeColor, int solidColor, float cornerRadius,
                                float topLeftRadius, float topRightRadius,
                                float bottomLeftRadius, float bottomRightRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(shape);
        drawable.setSize(10, 10);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setColor(solidColor);
        if (cornerRadius != 0) {
            drawable.setCornerRadius(cornerRadius);
        } else {
            drawable.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius,bottomLeftRadius, bottomLeftRadius});
        }
        return drawable;
    }

    /**
     * 获取填充颜色
     *
     * @return
     */
    public int getSolid() {
        return solid;
    }

    /**
     * 设置填充颜色
     *
     * @param solid
     */
    public void setSolid(int solid) {
        this.solid = solid;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取线宽
     *
     * @return
     */
    public int getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * 设置线宽
     *
     * @param strokeWidth
     */
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取线颜色
     *
     * @return
     */
    public int getStrokeColor() {
        return strokeColor;
    }

    /**
     * 设置线颜色
     *
     * @param strokeColor
     */
    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取图形
     *
     * @return
     */
    public int getShape() {
        return shape;
    }

    /**
     * 设置图形
     *
     * @param shape {@link GradientDrawable#RECTANGLE}  or {@link GradientDrawable#OVAL}
     */
    public void setShape(int shape) {
        this.shape = shape;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取半径
     *
     * @return
     */
    public float getRadius() {
        return radius;
    }

    /**
     * 设置四个方向圆角
     *
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取顶部左边圆角
     *
     * @return
     */
    public float getTopLeftRadius() {
        return topLeftRadius;
    }

    /**
     * 设置顶部左边圆角
     *
     * @param topLeftRadius
     */
    public void setTopLeftRadius(float topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取顶部右边圆角
     *
     * @return
     */
    public float getTopRightRadius() {
        return topRightRadius;
    }

    /**
     * 设置顶部右边圆角
     *
     * @param topRightRadius
     */
    public void setTopRightRadius(float topRightRadius) {
        this.topRightRadius = topRightRadius;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取底部左边圆角
     *
     * @return
     */
    public float getBottomLeftRadius() {
        return bottomLeftRadius;
    }

    /**
     * 设置底部左边圆角
     *
     * @param bottomLeftRadius
     */
    public void setBottomLeftRadius(float bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取底部左边圆角
     *
     * @return
     */
    public float getBottomRightRadius() {
        return bottomRightRadius;
    }

    /**
     * 设置底部右边圆角
     *
     * @param bottomRightRadius
     */
    public void setBottomRightRadius(float bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取饱和度
     *
     * @return
     */
    public float getSaturation() {
        return saturation;
    }

    /**
     * 设置饱和度
     *
     * @param saturation
     */
    public void setSaturation(float saturation) {
        this.saturation = saturation;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取普通状态Drawable
     *
     * @return
     */
    public Drawable getNormalDrawable() {
        return normalDrawable;
    }

    /**
     * 设置普通状态Drawable
     *
     * @param normalDrawable
     */
    public void setNormalDrawable(Drawable normalDrawable) {
        this.normalDrawable = normalDrawable;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取按下状态Drawable
     *
     * @return
     */
    public Drawable getPressedDrawable() {
        return pressedDrawable;
    }

    /**
     * 设置按下状态Drawable
     *
     * @param pressedDrawable
     */
    public void setPressedDrawable(Drawable pressedDrawable) {
        this.pressedDrawable = pressedDrawable;
        drawDrawable();
        invalidate();
    }

    /**
     * 获取状态
     *
     * @return
     */
    public int getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state {@link #STATE_TEXT} or {@link #STATE_BUTTON}
     */
    public void setState(int state) {
        this.state = state;
    }

}
