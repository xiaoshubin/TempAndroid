package com.smallcake.temp.utils

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

/**

【轮播图工具类】
使用方法：
val imgs = arrayListOf(
"https://ss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3de9ba8ee1194eef01f3a2979a8.jpg",
"https://gss0.baidu.com/70cFfyinKgQFm2e88IuM_a/forum/w=580/sign=6e3b71dd9f2f07085f052a08d926b865/25d69eec08fa513dcf7a0f4c3c6d55fbb3fbd92e.jpg"
)
bindBanner(this, bind.banner, imgs)

 * @param context AppCompatActivity
 * @param banner Banner
 * @param datas List<String>
 */
object BannerUtils{
    fun bindBanner(context: AppCompatActivity, banner: Banner<*, *>, datas: List<String>) {
        banner.adapter = ImgBannerAdapter(datas)
        banner.addBannerLifecycleObserver(context)
        banner.indicator = CircleIndicator(context)
    }
}

/**
 * 传入String类型的网络图片地址
 */
class ImgBannerAdapter constructor(datas: List<String>) :
    BannerAdapter<String, BannerImageHolder>(datas) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerImageHolder {
        val imageView = ImageView(parent.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerImageHolder(imageView)
    }

    override fun onBindView(holder: BannerImageHolder?, data: String, position: Int, size: Int) {
        holder?.let {
            holder.imageView.load(data)
        }

    }
}
