package com.smallcake.smallutils

import android.graphics.drawable.Drawable
import android.widget.TextView

object TextViewUtils {
     fun drawLeftIcon(tv: TextView, icon: Int) {
        val img: Drawable = tv.resources.getDrawable(icon)
        img.setBounds(0, 0, img.minimumWidth, img.minimumHeight)
        tv.setCompoundDrawables(img, null, null, null)
    }
}