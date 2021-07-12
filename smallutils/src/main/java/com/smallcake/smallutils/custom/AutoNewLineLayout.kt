package com.smallcake.smallutils.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.smallcake.smallutils.R

/**
 * Date:2021/7/12 10:30
 * Author:SmallCake
 * Desc:
 */
class AutoNewLineLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    /**
     * 两个子控件之间的横向间隙
     */
    protected var horizontalSpace = 0f

    /**
     * 两个子控件之间的垂直间隙
     */
    protected var vertivalSpace = 0f
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        //AT_MOST
        var width = 0
        var height = 0
        var rawWidth = 0 //当前行总宽度
        var rawHeight = 0 // 当前行高
        var rowIndex = 0 //当前行位置
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == GONE) {
                if (i == count - 1) {
                    //最后一个child
                    height += rawHeight
                    width = Math.max(width, rawWidth)
                }
                continue
            }

            //这里调用measureChildWithMargins 而不是measureChild
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val lp = child.layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            if (rawWidth + childWidth + (if (rowIndex > 0) horizontalSpace else 0f) > widthSpecSize - paddingLeft - paddingRight) {
                //换行
                width = Math.max(width, rawWidth)
                rawWidth = childWidth
                height += (rawHeight + vertivalSpace).toInt()
                rawHeight = childHeight
                rowIndex = 0
            } else {
                rawWidth += childWidth
                if (rowIndex > 0) {
                    rawWidth += horizontalSpace.toInt()
                }
                rawHeight = Math.max(rawHeight, childHeight)
            }
            if (i == count - 1) {
                width = Math.max(rawWidth, width)
                height += rawHeight
            }
            rowIndex++
        }
        setMeasuredDimension(
            if (widthSpecMode == MeasureSpec.EXACTLY) widthSpecSize else width + paddingLeft + paddingRight,
            if (heightSpecMode == MeasureSpec.EXACTLY) heightSpecSize else height + paddingTop + paddingBottom
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val viewWidth = r - l
        var leftOffset = paddingLeft
        var topOffset = paddingTop
        var rowMaxHeight = 0
        var rowIndex = 0 //当前行位置
        var childView: View
        var w = 0
        val count = childCount
        while (w < count) {
            childView = getChildAt(w)
            if (childView.visibility == GONE) {
                w++
                continue
            }
            val lp = childView.layoutParams as MarginLayoutParams
            // 如果加上当前子View的宽度后超过了ViewGroup的宽度，就换行
            val occupyWidth = lp.leftMargin + childView.measuredWidth + lp.rightMargin
            if (leftOffset + occupyWidth + paddingRight > viewWidth) {
                leftOffset = paddingLeft // 回到最左边
                topOffset += (rowMaxHeight + vertivalSpace).toInt() // 换行
                rowMaxHeight = 0
                rowIndex = 0
            }
            val left = leftOffset + lp.leftMargin
            val top = topOffset + lp.topMargin
            val right = leftOffset + lp.leftMargin + childView.measuredWidth
            val bottom = topOffset + lp.topMargin + childView.measuredHeight
            childView.layout(left, top, right, bottom)

            // 横向偏移
            leftOffset += occupyWidth
            // 试图更新本行最高View的高度
            val occupyHeight = lp.topMargin + childView.measuredHeight + lp.bottomMargin
            if (rowIndex != count - 1) {
                leftOffset += horizontalSpace.toInt()
            }
            rowMaxHeight = Math.max(rowMaxHeight, occupyHeight)
            rowIndex++
            w++
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.AutoNewLineLayout)
        horizontalSpace = ta.getDimension(R.styleable.AutoNewLineLayout_horizontalSpace, 0f)
        vertivalSpace = ta.getDimension(R.styleable.AutoNewLineLayout_vertivalSpace, 0f)
        ta.recycle()
    }
}