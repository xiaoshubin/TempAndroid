package com.smallcake.smallutils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File


/**
 * Date:2021/6/18 17:32
 * Author:SmallCake
 * Desc:
 **/
object SdUtils {
    /**
     * sd卡获取根目录路径
     * @return /storage/emulated/0/
     */
    fun getRootPath(): String? {
        return Environment.getExternalStorageDirectory().absolutePath + File.separator
    }

    /**
     * sd卡获取下载路径
     * @return /storage/emulated/0/Download/
     */
    fun getDownloadPath(): String? {
        return Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator
    }

    /**
     * 相机存放图片路径
     * @return /storage/emulated/0/DCIM/Camera/
     */
    fun getCameraPath(): String? {
        return (Environment.getExternalStorageDirectory().absolutePath
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator.toString() + "Camera" + File.separator)
    }

    /**
     * Uri photoUri = data.getData();
     * 根据相册返回的Uri获取照片路径
     * @return
     */
    fun getResultPhotoPath(activity: Context, photoUri: Uri): String? {
        val filePathColumn = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor: Cursor? = activity.contentResolver.query(photoUri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val photoPath: String? = cursor?.getString(cursor.getColumnIndex(filePathColumn[0]))
        cursor?.close()
        return photoPath
    }

}