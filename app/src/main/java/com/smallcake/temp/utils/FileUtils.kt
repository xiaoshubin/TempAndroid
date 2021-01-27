package com.smallcake.temp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * MyApplication --  com.smallcake.utils
 * Created by Small Cake on  2017/8/31 15:52.
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
     */
    fun openFilePath(context: Context, path: String?) {
        //getUrl()获取文件目录，例如返回值为/storage/sdcard1/MIUI/music/mp3_hd/单色冰淇凌_单色凌.mp3
        val file = File(path)
        //获取父目录
        val parentFlie = File(file.parent)
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setDataAndType(Uri.fromFile(parentFlie), "*/*")
        context.startActivity(intent)
    }


    //获取文件大小
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
}