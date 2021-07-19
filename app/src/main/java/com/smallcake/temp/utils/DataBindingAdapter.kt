package com.smallcake.temp.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

/**
 * Date: 2020/1/15
 * author: SmallCake
 * 注意使用条件：需要在在layout包裹的xml中使用
 * ImageView控件中url = "@{item.img_path}"
 * 必须使用@JvmStatic标记，否则在xml文件中使用会报错
 */
object DataBindingAdapter {
    //普通网络图片
    @JvmStatic
    @BindingAdapter("url")
    fun bindUrl(view: ImageView, imageUrl: String?) {
        view.load(imageUrl)
    }

    @JvmStatic
    @BindingAdapter(value = ["imgUrl", "placeholder"],requireAll = false)
    fun imgUrl(view: ImageView, url: String?,placeDrawable: Drawable) {
        view.load(url){
            placeholder(placeDrawable)
        }
    }





}