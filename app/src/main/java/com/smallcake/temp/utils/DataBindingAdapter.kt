package com.smallcake.temp.utils

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.util.Util
import com.smallcake.temp.MyApplication
import com.smallcake.temp.R
import com.smallcake.temp.utils.DpPxUtils.dp2pxFloat
import java.security.MessageDigest

/**
 * Date: 2020/1/15
 * author: SmallCake
 * 注意使用条件：需要在在layout包裹的xml中使用
 * ImageView控件中url = "@{item.img_path}"
 */
object DataBindingAdapter {
    //普通网络图片
    @BindingAdapter("url")
    fun bindUrl(view: ImageView?, imageUrl: String?) {
        Glide.with(view!!)
            .load(imageUrl)
            .into(view)
    }

    //圆形图片
    @BindingAdapter("circleUrl")
    fun bindImageCircleUrl(view: ImageView?, imageUrl: String?) {
        val options = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .circleCrop()
        Glide.with(view!!)
            .load(imageUrl)
            .apply(options)
            .into(view)
    }

    //圆角图片,圆角系数,默认为9
    @BindingAdapter(value = ["roundUrl", "roundRadius"], requireAll = false)
    fun bindImageRoundUrl(
        view: ImageView?,
        imageRoundUrl: String?,
        roundingRadius: Int
    ) {
        val options = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .transform(
                CenterCrop(),
                RoundedCorners(dp2pxFloat(if (roundingRadius == 0) 9f else roundingRadius.toFloat()).toInt())
            )
        Glide.with(view!!)
            .load(imageRoundUrl)
            .apply(options)
            .into(view)
    }

    /**
     * 上部为圆角的图片样式，
     * @param view ImageView
     * @param imageRoundUrl  要加载的图片url
     * @param roundingRadius 圆角弧度 默认9
     */
    @BindingAdapter(value = ["topRoundUrl", "roundRadius"], requireAll = false)
    fun bindImageTopRoundUrl(
        view: ImageView?,
        imageRoundUrl: String?,
        roundingRadius: Float
    ) {
        val transformation = CornerTransform(
            MyApplication.instance,
            dp2pxFloat(if (roundingRadius == 0f) 9f else roundingRadius)
        )
        transformation.setExceptCorner(true, true, false, false)
        Glide.with(MyApplication.instance).load(imageRoundUrl)
            .apply(RequestOptions().transform(transformation)).into(view!!)
    }
}

/**
 * Date: 2020/1/15
 * author: SmallCake
 * 自定义圆角方位
 */
internal class CornerTransform(
    context: Context?,
    radius: Float
) : Transformation<Bitmap?> {
    private val mBitmapPool: BitmapPool
    private var radius: Float
    private var exceptLeftTop = false
    private var exceptRightTop = false
    private var exceptLeftBottom = false
    private var exceptRightBotoom = false

    /**
     * 需要圆角的位置
     *
     * @param leftTop
     * @param rightTop
     * @param leftBottom
     * @param rightBottom
     */
    fun setExceptCorner(
        leftTop: Boolean,
        rightTop: Boolean,
        leftBottom: Boolean,
        rightBottom: Boolean
    ) {
        exceptLeftTop = leftTop
        exceptRightTop = rightTop
        exceptLeftBottom = leftBottom
        exceptRightBotoom = rightBottom
    }

    val id: String
        get() = this.javaClass.name

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
    override fun hashCode(): Int {
        //避免Transformation重复设置,导致图片闪烁,同一个圆角值的Transformation视为同一个对象
        return Util.hashCode(
            id.hashCode(),
            Util.hashCode(radius)
        )
    }

    override fun transform(
        context: Context,
        resource: Resource<Bitmap?>,
        outWidth: Int,
        outHeight: Int
    ): Resource<Bitmap?> {
        val source = resource.get()
        var finalWidth: Int
        var finalHeight: Int
        var ratio: Float //输出目标的宽高或高宽比例
        if (outWidth > outHeight) { //输出宽度>输出高度,求高宽比
            ratio = outHeight.toFloat() / outWidth.toFloat()
            finalWidth = source.width
            finalHeight = (source.width.toFloat() * ratio).toInt() //固定原图宽度,求最终高度
            if (finalHeight > source.height) { //求出的最终高度>原图高度,求宽高比
                ratio = outWidth.toFloat() / outHeight.toFloat()
                finalHeight = source.height
                finalWidth = (source.height.toFloat() * ratio).toInt() //固定原图高度,求最终宽度
            }
        } else if (outWidth < outHeight) { //输出宽度 < 输出高度,求宽高比
            ratio = outWidth.toFloat() / outHeight.toFloat()
            finalHeight = source.height
            finalWidth = (source.height.toFloat() * ratio).toInt() //固定原图高度,求最终宽度
            if (finalWidth > source.width) { //求出的最终宽度 > 原图宽度,求高宽比
                ratio = outHeight.toFloat() / outWidth.toFloat()
                finalWidth = source.width
                finalHeight = (source.width.toFloat() * ratio).toInt()
            }
        } else { //输出宽度=输出高度
            finalHeight = source.height
            finalWidth = finalHeight
        }

        //修正圆角
        radius *= finalHeight.toFloat() / outHeight.toFloat()
        var outBitmap: Bitmap? =
            mBitmapPool[finalWidth, finalHeight, Bitmap.Config.ARGB_8888]
        if (outBitmap == null) {
            outBitmap = Bitmap.createBitmap(finalWidth, finalHeight, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(outBitmap!!)
        val paint = Paint()
        //关联画笔绘制的原图bitmap
        val shader =
            BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        //计算中心位置,进行偏移
        val width = (source.width - finalWidth) / 2
        val height = (source.height - finalHeight) / 2
        if (width != 0 || height != 0) {
            val matrix = Matrix()
            matrix.setTranslate((-width).toFloat(), (-height).toFloat())
            shader.setLocalMatrix(matrix)
        }
        paint.shader = shader
        paint.isAntiAlias = true
        val rectF =
            RectF(0.0f, 0.0f, canvas.width.toFloat(), canvas.width.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint) //先绘制圆角矩形
        if (!exceptLeftTop) { //左上角不为圆角
            canvas.drawRect(0f, 0f, radius, radius, paint)
        }
        if (!exceptRightTop) { //右上角不为圆角
            canvas.drawRect(
                canvas.width - radius,
                0f,
                canvas.width.toFloat(),
                radius,
                paint
            )
        }
        if (!exceptLeftBottom) { //左下角不为圆角
            canvas.drawRect(
                0f,
                canvas.height - radius,
                radius,
                canvas.height.toFloat(),
                paint
            )
        }
        if (!exceptRightBotoom) { //右下角不为圆角
            canvas.drawRect(
                canvas.width - radius,
                canvas.height - radius,
                canvas.width.toFloat(),
                canvas.height.toFloat(),
                paint
            )
        }
        return BitmapResource.obtain(outBitmap, mBitmapPool)!!
    }

    init {
        mBitmapPool = Glide.get(context!!).bitmapPool
        this.radius = radius
    }
}