package com.smallcake.temp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.smallcake.temp.MyApplication
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * MyApplication --  com.smallcake.utils
 * Created by Small Cake on  2017/8/31 15:52.
 * 文件工具类
 */
object FileUtils {
    /**
     * 创建文件夹,如果不存在的话
     * @param filePath
     * @return
     */
    fun makeDirs(filePath: String?): Boolean {
        if (filePath == null || filePath.isEmpty()) {
            return false
        }
        val folder = File(filePath)
        return if (folder.exists() && folder.isDirectory) true else folder.mkdirs()
    }

    /**
     * 创建一个文件的上级目录，如果不存在的话
     * @param filePath
     * @return
     */
    fun makeParentDirs(filePath: String?, fileName: String?): Boolean {
        if (filePath == null || filePath.isEmpty()) {
            return false
        }
        val folder = File(filePath, fileName)
        return if (folder.parentFile.exists() && folder.parentFile
                .isDirectory
        ) true else folder.parentFile.mkdirs()
    }

    /**
     * 打开指定路径目录
     * @param path
     * @Deprecated : 10已经不允许直接打开了，或者有其他方法，找寻中...
     */
    fun openFilePath(context: Context, path: String?) {
        //getUrl()获取文件目录，例如返回值为/storage/sdcard1/MIUI/music/mp3_hd/单色冰淇凌_单色凌.mp3
        val file = File(path)
        //获取父目录
        val parentFlie = File(file.parent)
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        var data:Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           FileProvider.getUriForFile(context,"${AppUtils.getAppPackageName()}.fileprovider",parentFlie)
        }else Uri.fromFile(parentFlie)
        intent.setDataAndType(data, "*/*")
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.message?.let { lee(it) }
            showToast("未找到此文件夹！")
        }
    }


    /**
     * 获取文件大小
     */
    fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            try {
                val fis = FileInputStream(file)
                size = fis.available().toLong()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            showToast("文件不存在！")
        }
        return size
    }

    /**
     * 保存图片到手机应用缓存目录，无需存储权限
     * @param bitmap Bitmap
     * @param activity Activity
     */
    fun saveBitmap(bitmap: Bitmap): String {
        val externalCacheDir = MyApplication.instance.externalCacheDir
        val file = File(externalCacheDir, "${System.currentTimeMillis()}.jpg")
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
        fos.flush()
        fos.close()
        return file.path
    }
}