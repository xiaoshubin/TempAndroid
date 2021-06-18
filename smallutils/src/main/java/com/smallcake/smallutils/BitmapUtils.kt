package com.smallcake.smallutils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.os.Environment
import android.util.Log
import android.view.View
import java.io.*


/**
 * Date:2021/6/18 16:38
 * Author:SmallCake
 * Desc:
 **/
object BitmapUtils {


    /**
     * 路径获取图片转Bitmap
     * @param imgPath
     * @return
     */
    fun getBitmapPath(imgPath: String?): Bitmap? {
        return BitmapFactory.decodeFile(imgPath)
    }

    /**
     * 从资源id中获取Bitmap
     * @param res
     * @return
     */
    fun getBitmapRes(res: Int): Bitmap? {
        return BitmapFactory.decodeResource(SmallUtils.context?.resources, res)
    }

    /**
     * 保存位图图像到指定的路径
     *
     * @param bitmap
     * @param outPath
     * @throws FileNotFoundException
     */
    @Throws(FileNotFoundException::class)
    fun saveBitmap(bitmap: Bitmap, outPath: String?): Boolean {
        return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(outPath))
    }

    /**
     *
     * @param bitmap Bitmap
     * @return Boolean
     * @throws FileNotFoundException
     */
    @Throws(FileNotFoundException::class)
    fun saveBitmapInPictures(bitmap: Bitmap): Boolean {
         val cameraPath = Environment.getExternalStorageDirectory().absolutePath+ File.separator + Environment.DIRECTORY_DCIM+ File.separator + "Camera" + File.separator
        val file = File(cameraPath,"${System.currentTimeMillis()}.jpg")
        Log.i("BitmapUtils","保存图片路径：${file.absoluteFile}")
        return bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            FileOutputStream(file)
        )
    }

    /**
     * 打印获取的Bitmap大小和尺寸
     * @param bitmap
     */
    fun printBitmapSize(bitmap: Bitmap) {
        Log.i(
            "BitmapUtils",
            "Bitmap的大小【" + bitmap.byteCount + "】byte 宽度【" + bitmap.width + "】高度【" + bitmap.height + "】"
        )
    }

    /**
     * TODO 1 质量压缩 ,bitmap所占内存大小是不会变的,返回字节数组
     * 不会减少图片的像素，保持像素的前提下改变图片的位深及透明度等
     *
     * 但是我们看到bytes.length是随着quality变小而变小的。
     * 这样适合去传递二进制的图片数据，比如[微信分享图片]，要传入二进制数据过去，限制32kb之内。
     *
     * @注意： 如果是 bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
     * 这样的png格式，quality就没有作用了，bytes.length不会变化，因为png图片是无损的，不能进行压缩。
     *
     * 还有一种格式，CompressFormat.WEBP格式，该格式是google自己推出来一个图片格式
     */
    fun compressQuality(bitmap: Bitmap, quality: Int): ByteArray? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        return baos.toByteArray()
    }

    /**
     * TODO 2.采样率压缩,bitmap所占内存根据inSampleSize变小
     * @param context
     * @param res
     * @param inSampleSize 传入2，就压缩为原来的 1/2
     * 设置inSampleSize的值(int类型)后，假如设为2，则宽和高都为原来的1/2，宽高都减少了，自然内存也降低了。
     *
     * @注意 我上面的代码没用过options.inJustDecodeBounds = true;
     * 因为我是固定来取样的数据，为什么这个压缩方法叫采样率压缩，
     * 是因为配合inJustDecodeBounds，先获取图片的宽、高【这个过程就是取样】，
     * 然后通过获取的宽高，动态的设置inSampleSize的值。
     *
     * 当inJustDecodeBounds设置为true的时候，BitmapFactory通过decodeResource或者decodeFile解码图片时，
     * 将会返回空(null)的Bitmap对象，这样可以避免Bitmap的内存分配，但是它可以返回Bitmap的宽度、高度以及MimeType。
     */
    fun compressRate(context: Context, res: Int, inSampleSize: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize
        return BitmapFactory.decodeResource(context.getResources(), res, options)
    }

    /**
     * TODO 3.缩放法压缩（Matrix矩阵），bitmap所占内存根据matrix缩放比例变小,比采样率压缩更小
     * @param context
     * @param res
     * @param sx
     * @param sy
     * @注意 如果传入的sx和sy大于1，也就是放大操作可能会出现内存不足异常
     * sx和sy不同的话，可能造成图片被拉伸或挤压，这也是它和采样率压缩不同的地方
     */
    fun compressMatrix(context: Context, res: Int, sx: Float, sy: Float): Bitmap? {
        val bit = setRGB_565(context, res)
        val matrix = Matrix()
        matrix.setScale(sx, sy)
        return Bitmap.createBitmap(bit, 0, 0, bit.width, bit.height, matrix, true)
    }

    fun compressMatrix(imgPath: String?, sx: Float, sy: Float): Bitmap? {
        val bit = setRGB_565(imgPath)
        val matrix = Matrix()
        matrix.setScale(sx, sy)
        return Bitmap.createBitmap(bit, 0, 0, bit.width, bit.height, matrix, true)
    }

    /**
     * TODO 4.把图片设置为RGB_565，相比argb_8888减少了一半的内存开销，且长度和宽度不变
     * @param context
     * @param res
     */
    fun setRGB_565(context: Context, res: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        return BitmapFactory.decodeResource(context.getResources(), res, options)
    }

    fun setRGB_565(pathName: String?): Bitmap {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        return BitmapFactory.decodeFile(pathName, options)
    }


    /**
     * TODO 5.压缩成用户希望的宽高
     * 首先把图片设置为RGB_565来减小大小
     * @param context
     * @param res
     * @param witdh
     * @param height
     * @注意 如果希望的宽高和原图差太多，图片会很不清晰
     * 如果宽高和原图宽高比不同，图片还会被挤压和拉伸
     */
    fun compressWH(context: Context, res: Int, witdh: Int, height: Int): Bitmap? {
        return Bitmap.createScaledBitmap(setRGB_565(context, res), witdh, height, true)
    }

    fun compressWH(pathName: String?, witdh: Int, height: Int): Bitmap? {
        return Bitmap.createScaledBitmap(setRGB_565(pathName), witdh, height, true)
    }

    fun compressImageReturnFile(filePath: String?, targetPath: String?, quality: Int): File? {
        val bm = setRGB_565(filePath)
        val outputFile = File(targetPath)
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs()
            } else {
                outputFile.delete()
            }
            val out = FileOutputStream(outputFile)
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out)
        } catch (e: Exception) {
        }
        return outputFile
    }

    fun bitmapToInputStream(bitmap: Bitmap): InputStream? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return ByteArrayInputStream(baos.toByteArray())
    }

    /**
     * view转bitmap
     * @param v View
     * @return Bitmap?
     * 出现异常：
     * java.lang.IllegalArgumentException: Software rendering doesn't support hardware bitmaps
     * 解决：设置Activity页面android:hardwareAccelerated="false"
     * 获取图片后，图片位置移位
     * 解决：视图外层包裹一层LinearLayout或者重新设置布局
     */
    fun getBitmapFromView(v: View): Bitmap? {
        val w: Int = v.width
        val h: Int = v.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)//如果不设置canvas画布为白色，则生成透明
        v.layout(0, 0, w, h)
        v.draw(c)
        return bmp
    }

    fun drawable2Bitmap(drawable: Drawable): Bitmap? {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else if (drawable is NinePatchDrawable) {
            val bitmap = Bitmap
                .createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    if (drawable.getOpacity() != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
                )
            val canvas = Canvas(bitmap)
            drawable.setBounds(
                0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight()
            )
            drawable.draw(canvas)
            bitmap
        } else {
            null
        }
    }
}