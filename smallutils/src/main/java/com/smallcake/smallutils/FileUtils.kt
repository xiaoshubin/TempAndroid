package com.smallcake.smallutils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.smallcake.smallutils.ToastUtil.Companion.showShort
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Date:2021/5/28 16:26
 * Author:SmallCake
 * Desc:
 **/
object FileUtils {
    /**
     * 如果文件不存在就创建它，否则什么也不做。
     *
     * @param file The file.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        if (file.exists()) return file.isFile
        return if (createOrExistsDir(file.parentFile)) false else try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 如果文件夹不存在就创建它，否则什么也不做。
     *
     * @param file The file.
     * @return `true`: exists or creates successfully<br></br>`false`: otherwise
     */
    fun createOrExistsDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }
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
        return if (folder.exists() && folder.isDirectory()) true else folder.mkdirs()
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
        return if (folder.getParentFile().exists() && folder.getParentFile()
                .isDirectory()
        ) true else folder.getParentFile().mkdirs()
    }

    /**
     * 打开指定路径目录
     * @param path
     */
    fun openFilePath(context: Context, path: String?) {
        //getUrl()获取文件目录，例如返回值为/storage/sdcard1/MIUI/music/mp3_hd/单色冰淇凌_单色凌.mp3
        val file = File(path)
        //获取父目录
        val parentFlie = File(file.getParent())
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setDataAndType(Uri.fromFile(parentFlie), "*/*")
        context.startActivity(intent)
    }

    /**
     *  获取文件大小
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
            showShort("文件不存在！")
        }
        return size
    }

    fun getFileSize(path: String?) = getFileSize(File(path))
}