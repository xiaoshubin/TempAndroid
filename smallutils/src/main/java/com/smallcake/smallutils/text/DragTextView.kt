package com.smallcake.smallutils.text

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.smallcake.smallutils.DragHelper

/**
 * 可推拽的文本
 */
class DragTextView : AppCompatTextView {
    private var dragHelper: DragHelper?= null

    constructor(context: Context?) : super(context!!) {
        initHelper()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        initHelper()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        initHelper()
    }
    private fun initHelper() {
        dragHelper = DragHelper(this)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        dragHelper?.onMeasure()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        return dragHelper?.onTouchEvent(event) ?: false
    }
}