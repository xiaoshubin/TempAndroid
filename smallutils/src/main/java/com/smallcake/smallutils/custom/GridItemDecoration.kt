package com.smallcake.smallutils.custom

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.smallcake.smallutils.DpUtils


/**
 * RecyclerView网格布局 （间隙|分割线）
 *
 * 横向和纵向的列表布局可以用官方的DividerItemDecoration
 * 参考：
 * 【自定义ItemDecoration分割线的高度、颜色、偏移，看完这个你就懂了】https://blog.csdn.net/Myfittinglife/article/details/88633980
 * 【RecyclerView之ItemDecoration由浅入深】https://www.jianshu.com/p/b46a4ff7c10a
 * 【网格布局GridItemDecoration】https://blog.csdn.net/q1113225201/article/details/84698300
 * @see spanCount        列数          一定要等于设置GridLayoutManager的列数
 * @see space            间隙大小       默认8dp
 * @see dividerColor     间隙颜色       默认透明
 * @see isBorder         是否需要外边框 默认需要外边框
 */

class GridItemDecoration(val spanCount:Int=1, space:Float=8f, @ColorInt dividerColor:Int=Color.TRANSPARENT,private val isBorder:Boolean=true): RecyclerView.ItemDecoration() {
    private val spaceSize = DpUtils.dp2px(space)
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    init {
        mPaint.apply {
            color = dividerColor
            strokeWidth = spaceSize.toFloat()
        }
    }
    override fun getItemOffsets(outRect: Rect,view: View,parent: RecyclerView,state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(spaceSize,spaceSize,spaceSize,spaceSize)
        val position = parent.getChildAdapterPosition(view)
        val row: Int = position / spanCount //行数
        val column: Int = position % spanCount //列数
        if (isBorder) {
            outRect.left = spaceSize - column * spaceSize / spanCount
            outRect.right = (column + 1) * spaceSize / spanCount
            //只有第一行我们才设置顶部边距，这样才不会出现中间的item间隙过大
            if (row==0)outRect.top = spaceSize else outRect.top=0
            outRect.bottom = spaceSize
        } else {
            outRect.left = column * spaceSize / spanCount
            outRect.right = spaceSize - (column + 1) * spaceSize / spanCount
            outRect.top = if (row==0)0 else spaceSize
            outRect.bottom = 0
        }

    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        for (i in 0 until parent.childCount){//可见ItemView个数
            val view = parent.getChildAt(i)
            val dividerLeft =  view.left-spaceSize
            val dividerTop = view.top - spaceSize
            val dividerRight = view.right + spaceSize
            val dividerBottom = view.bottom + spaceSize
            val rect = Rect(dividerLeft,dividerTop,dividerRight,if(isBorder) dividerBottom else view.bottom)
            c.drawRect(rect,mPaint)
        }

    }



}