/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright 2014 Manabu Shimobe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallcake.smallutils.text

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.smallcake.smallutils.R

/**
 * 可展开的文本控件
<com.smallcake.smallutils.text.ExpandableTextView
    android:id="@+id/tv_desc"
    xmlns:expandableTextView="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    expandableTextView:animDuration="300"
    expandableTextView:maxCollapsedLines="1">
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <TextView
        android:paddingTop="@dimen/dp_16"
        android:paddingBottom="@dimen/dp_16"
        android:paddingLeft="@dimen/dp_16"
        android:id="@id/expandable_text"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="文本内容，超过一行显示折叠图标，否则不显示折叠图标"
        android:textColor="#666666"
        android:textSize="16sp" />
    <ImageButton
        android:padding="@dimen/dp_16"
        android:layout_marginLeft="@dimen/dp_8"
        android:id="@id/expand_collapse"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
    </LinearLayout>
</com.smallcake.smallutils.text.ExpandableTextView>

 注意：
    1.里面的TextView的id必须为：android:id="@id/expandable_text"，ImageButton的id必须为：android:id="@id/expand_collapse"
    2.里面的TextView不能设置android:ellipsize="end"和android:maxLines="1"属性
 使用：
    0.必须在代码中使用此控件的setText重新设置文本内容
    1.maxCollapsedLines为收缩时显示行数
    2.expandDrawable为你自己的展开图标
    3.collapseDrawable为你自己的收缩图标
    4.ImageButton设置android:layout_gravity="right|bottom"，图标会随文字上下移动
 */
class ExpandableTextView : LinearLayout, View.OnClickListener {
    private val TAG = "ExpandableTextView"
    private var mTv: TextView? = null
    private var mButton : ImageButton? = null// Button to expand/collapse
    private var mRelayout = true //是否正在动态布局，
    private var mCollapsed = true // Show short version as default.
    private var mCollapsedHeight = 0
    private var mTextHeightWithMaxLines = 0
    private var mMaxCollapsedLines = 0
    private var mMarginBetweenTxtAndBottom = 0
    private var mExpandDrawable: Drawable? = null
    private var mCollapseDrawable: Drawable? = null
    private var mAnimationDuration = 0
    private var mAnimAlphaStart = 0f
    private var mAnimating = false
    private var mListener: ((TextView, Boolean) -> Unit?)? = null
    private var mCollapsedStatus: SparseBooleanArray? = null
    private var mPosition = 0

    @JvmOverloads
    constructor(context: Context?,attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?,attrs: AttributeSet?,defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    override fun setOrientation(orientation: Int) {
        require(HORIZONTAL != orientation) { "ExpandableTextView only supports Vertical Orientation." }
        super.setOrientation(orientation)
    }

    override fun onClick(view: View) {
        if (mButton!!.visibility != View.VISIBLE) {return}
        mCollapsed = !mCollapsed//收缩取反
        mButton!!.setImageDrawable(if (mCollapsed) mExpandDrawable else mCollapseDrawable)
        if (mCollapsedStatus != null) {mCollapsedStatus!!.put(mPosition, mCollapsed)}

        mAnimating = true
        val animation: Animation = if (mCollapsed) {
            ExpandCollapseAnimation(this, height, mCollapsedHeight)
        } else {
            ExpandCollapseAnimation(this, height, height +mTextHeightWithMaxLines - mTv!!.height)
        }
        animation.fillAfter = true
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                mTv?.let { applyAlphaAnimation(it, mAnimAlphaStart) }
            }
            override fun onAnimationEnd(animation: Animation) {
                clearAnimation()
                mAnimating = false
                mTv?.let { mListener?.invoke(it, !mCollapsed) }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        clearAnimation()
        startAnimation(animation)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return mAnimating
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViews()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        Log.d(TAG,"Relayout:${mRelayout}")
        if (!mRelayout || visibility == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        mRelayout = false
        mButton!!.visibility = View.GONE//默认隐藏收缩按钮，因为文本可能<=最大显示行数
        mTv!!.maxLines = Int.MAX_VALUE
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mTv!!.lineCount <= mMaxCollapsedLines) {
            return
        }
        if (mCollapsed) {
            mTv!!.maxLines = mMaxCollapsedLines
//            mTv!!.ellipsize = TextUtils.TruncateAt.END
        }
        mButton!!.visibility = View.VISIBLE
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mTextHeightWithMaxLines = getRealTextViewHeight(mTv!!)
        if (mCollapsed) {
            mTv!!.post { mMarginBetweenTxtAndBottom = height - mTv!!.height }
            mCollapsedHeight = measuredHeight
        }
    }

    fun setOnExpandStateChangeListener(listener:(TextView, Boolean) -> Unit?) {
        mListener = listener
    }

    fun setText(text: CharSequence?,collapsedStatus: SparseBooleanArray,position: Int) {
        mCollapsedStatus = collapsedStatus
        mPosition = position
        val isCollapsed = collapsedStatus[position, true]
        clearAnimation()
        mCollapsed = isCollapsed
        mButton!!.setImageDrawable(if (mCollapsed) mExpandDrawable else mCollapseDrawable)
        this.text = text
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        requestLayout()
    }

    var text: CharSequence?
        get() = if (mTv == null) {""} else mTv!!.text
        set(text) {
            mRelayout = true
            mTv!!.text = text
            visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
        }

    private fun init(attrs: AttributeSet?) {
        val typedArray =context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines,MAX_COLLAPSED_LINES)
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration,DEFAULT_ANIM_DURATION)
        mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandableTextView_animAlphaStart,DEFAULT_ANIM_ALPHA_START)
        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable)?:getDrawable(context,android.R.drawable.arrow_up_float)
        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable)?:getDrawable(context,android.R.drawable.arrow_down_float)
        typedArray.recycle()
        orientation = VERTICAL
//        visibility = View.VISIBLE
    }

    private fun findViews() {
        mTv = findViewById<View>(R.id.expandable_text) as TextView
        mTv!!.setOnClickListener(this)
        mButton = findViewById<View>(R.id.expand_collapse) as ImageButton
        mButton!!.setImageDrawable(if (mCollapsed) mExpandDrawable else mCollapseDrawable)
        mButton!!.setOnClickListener(this)
    }

    internal inner class ExpandCollapseAnimation(private val mTargetView: View,private val mStartHeight: Int,private val mEndHeight: Int) : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            val newHeight =
                ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight).toInt()
            mTv!!.maxHeight = newHeight - mMarginBetweenTxtAndBottom
            if (mAnimAlphaStart.compareTo(1.0f) != 0) {
                applyAlphaAnimation(
                    mTv!!,mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart)
                )
            }
            mTargetView.layoutParams.height = newHeight
            mTargetView.requestLayout()
        }
        override fun willChangeBounds(): Boolean {
            return true
        }
        init {
            duration = mAnimationDuration.toLong()
        }
    }

    interface OnExpandStateChangeListener {
        fun onExpandStateChanged(textView: TextView?, isExpanded: Boolean)
    }

    companion object {
        private const val MAX_COLLAPSED_LINES = 8
        private const val DEFAULT_ANIM_DURATION = 300
        private const val DEFAULT_ANIM_ALPHA_START = 0.7f

        private fun applyAlphaAnimation(view: View,alpha: Float) {
                view.alpha = alpha
        }

        private fun getDrawable(context: Context,@DrawableRes resId: Int): Drawable {
            val resources = context.resources
            return  resources.getDrawable(resId, context.theme)

        }

        private fun getRealTextViewHeight(textView: TextView): Int {
            val textHeight = textView.layout.getLineTop(textView.lineCount)
            val padding =
                textView.compoundPaddingTop + textView.compoundPaddingBottom
            return textHeight + padding
        }
    }
}