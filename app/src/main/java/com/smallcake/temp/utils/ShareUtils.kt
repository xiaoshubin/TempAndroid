package com.smallcake.temp.utils

import android.app.Activity
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.smallcake.temp.MyApplication
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * Project20180408 --  cn.com.smallcake_utils
 * Created by Small Cake on  2018/5/4 15:50.
 * 分享工具类
 *
 *
 * 注意：
 * 1.分享图片需要
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
 *
 *
 * 2.由于分享功能是使用隐式调用Activtiy实现的，则需在响应的Activity中声明intent-filter，在对应的activity的xml里加上
 *
 *
 * <intent-filter>
 * <action android:name="android.intent.action.SEND"></action>
 * <category android:name="android.intent.category.DEFAULT"></category>
</intent-filter> *
 *
 *
 * 3.开启另一个分享Acivity需要Activity的上下文
 */
object ShareUtils {
    /**
     * 分享文字内容
     *
     * @param dlgTitle 分享对话框标题
     * @param subject  主题
     * @param content  分享内容（文字）
     */
    fun shareText(
        activity: Activity,
        dlgTitle: String,
        subject: String,
        content: String
    ) {
        if ("" == content) {
            return
        }
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        if ("" != subject) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        intent.putExtra(Intent.EXTRA_TEXT, content)

        // 设置弹出框标题
        if ("" != dlgTitle) { // 自定义标题
            activity.startActivity(Intent.createChooser(intent, dlgTitle))
        } else { // 系统默认标题
            activity.startActivity(intent)
        }
    }

    /**
     * 分享图片和文字内容
     *
     * @param dlgTitle 分享对话框标题 ：弹出的分享Dialog上面标题
     * @param subject  主题：一般只有发送邮件才会用到此参数
     * @param content  分享内容（文字）：微博才有
     * @param uri      图片资源URI
     */
    fun shareImg(
        activity: Activity,
        dlgTitle: String,
        subject: String,
        content: String,
        uri: Uri?
    ) {
        if (uri == null) return
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        if (!TextUtils.isEmpty(subject)) intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (!TextUtils.isEmpty(content)) intent.putExtra(Intent.EXTRA_TEXT, content)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) intent.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        // 设置弹出框标题
        if ("" != dlgTitle) { // 自定义标题
            activity.startActivity(Intent.createChooser(intent, dlgTitle))
        } else { // 系统默认标题
            activity.startActivity(intent)
        }
    }

    fun shareImgToWechat(
        activity: Activity,
        dlgTitle: String?,
        subject: String?,
        content: String,
        uri: Uri?
    ) {
        if (uri == null) return
        val comp =
            ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI")
        val shareIntent = Intent()
        shareIntent.component = comp
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        if (!TextUtils.isEmpty(subject)) shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (!TextUtils.isEmpty(content)) shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) shareIntent.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        if (TextUtils.isEmpty(dlgTitle)) { // 自定义标题
            activity.startActivity(Intent.createChooser(shareIntent, dlgTitle))
        } else { // 系统默认标题
            activity.startActivity(shareIntent)
        }
    }

    fun shareImg(
        activity: Activity,
        dlgTitle: String,
        subject: String,
        content: String,
        bitmap: Bitmap
    ) {
        val uri = saveBitmap(activity, bitmap, content) ?: return
        shareImg(activity, dlgTitle, subject, content, uri)
    }

    //分享图片到微信
    fun shareImgToWechatMoments(
        activity: Activity,
        dlgTitle: String,
        subject: String,
        content: String,
        bitmap: Bitmap
    ) {
        shareImgToOtherApp(0, activity, dlgTitle, subject, content, bitmap)
    }

    fun shareImgToWechat(
        activity: Activity,
        dlgTitle: String,
        subject: String,
        content: String,
        bitmap: Bitmap
    ) {
        shareImgToOtherApp(1, activity, dlgTitle, subject, content, bitmap)
    }

    fun shareImgToQQ(
        activity: Activity,
        dlgTitle: String,
        subject: String,
        content: String,
        bitmap: Bitmap
    ) {
        shareImgToOtherApp(2, activity, dlgTitle, subject, content, bitmap)
    }

    fun shareImgToSina(
        activity: Activity,
        dlgTitle: String,
        subject: String,
        content: String,
        bitmap: Bitmap
    ) {
        shareImgToOtherApp(3, activity, dlgTitle, subject, content, bitmap)
    }

    /**
     * 注意：qq空间页面activity不对外开放
     * 所以qq空间分享只支持sdk分享，不支持原生qq空间分享
     */
    fun shareImgToOtherApp(
        type: Int,
        activity: Activity,
        dlgTitle: String,
        subject: String,
        content: String,
        bitmap: Bitmap
    ) {
        val uri = saveBitmap(activity, bitmap, content) ?: return
        var comp =
            ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI")
        when (type) {
            0 -> comp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI")
            1 -> comp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI")
            2 -> comp =
                ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity")
            3 -> comp = ComponentName(
                "com.sina.weibo",
                "com.sina.weibo.composerinde.ComposerDispatchActivity"
            )
        }
        val shareIntent = Intent()
        shareIntent.component = comp
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        if (!TextUtils.isEmpty(subject)) shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (!TextUtils.isEmpty(content)) shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) shareIntent.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        try {
            if (TextUtils.isEmpty(dlgTitle)) { // 自定义标题
                activity.startActivity(Intent.createChooser(shareIntent, dlgTitle))
            } else { // 系统默认标题
                activity.startActivity(shareIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("没有安装此应用！")
        }
    }

    /**
     * 将图片存到本地 不能保存到缓存路径，因为只有本应用才能访问
     */
    private fun saveBitmap(
        activity: Activity,
        bm: Bitmap,
        imgName: String
    ): Uri? {
        try {
            val cacheDir = activity.externalCacheDir!!.path
            val dir =
                cacheDir + System.currentTimeMillis() + "_" + imgName + ".jpg"
            val f = File(dir)
            if (!f.exists()) {
                f.parentFile.mkdirs()
                f.createNewFile()
            }
            val out = FileOutputStream(f)
            bm.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            val uri: Uri
            //7.0以上需要添加临时读取权限
            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    activity,MyApplication.instance.toString() + ".fileprovider",
                    f
                )
            } else {
                Uri.fromFile(f)
            }
            return uri
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 分享多张图片
     */
    fun shareMultipleImage(activity: Activity, vararg files: File?) {
        val uriList = ArrayList<Uri>()
        for (file in files) uriList.add(Uri.fromFile(file))
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND_MULTIPLE
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
        shareIntent.type = "image/*"
        activity.startActivity(Intent.createChooser(shareIntent, "分享到"))
    }

    fun shareTxtAndImg(
        activity: Activity,
        activityTitle: String,
        msgTitle: String,
        msgText: String,
        imgPath: String
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        if (imgPath == "") {
            intent.type = "text/plain" // 纯文本
        } else {
            val f = File(imgPath)
            if (f.exists() && f.isFile) {
                intent.type = "image/jpg"
                val u = Uri.fromFile(f)
                intent.putExtra(Intent.EXTRA_STREAM, u)
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle)
        intent.putExtra(Intent.EXTRA_TEXT, msgText)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(Intent.createChooser(intent, activityTitle))
    }

    fun shareTxtAndImg(
        activity: Activity,
        activityTitle: String?,
        msgTitle: String?,
        msgText: String?,
        imgRes: Int
    ) {
        val imageUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + activity.resources.getResourcePackageName(imgRes) + "/"
                    + activity.resources.getResourceTypeName(imgRes) + "/"
                    + activity.resources.getResourceEntryName(imgRes)
        )
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/jpg"
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle)
        intent.putExtra(Intent.EXTRA_TEXT, msgText)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(Intent.createChooser(intent, activityTitle))
    }
}