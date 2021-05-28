package com.smallcake.smallutils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Date:2021/5/28 16:40
 * Author:SmallCake
 * Desc:
 **/
object CameraUtils {
    val REQUEST_CAMERA = 909

    /**
     * 调用系统摄像头拍照
     * @param activity 页面
     * @param picSavePath 图片路径，如：/storage/emulated/0/Android/data/com.yx.driver.training/cache/2021-05-28_16-47-51.jpg
     * @param cameraFacing true:前置摄像头 false:后置摄像头 默认false
     * 注意使用
     * 1.picSavePath 首先要创建好然后传入，确定路径正确
     * 2.回调注意判断if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {
            Log.i("图片地址",picSavePath")
        }

     }


     */
    fun tackPhoto(activity: Activity,picSavePath:String,cameraFacing: Boolean=false) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraFacing)intent.putExtra("android.intent.extras.CAMERA_FACING", 1)//调用前置摄像头
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val uriForFile: Uri =FileProvider.getUriForFile(activity, "${activity.packageName}.fileprovider", File(picSavePath))
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile)
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(picSavePath)))
        }
        activity.startActivityForResult(intent, REQUEST_CAMERA)
    }

    @SuppressLint("SimpleDateFormat")
    fun getFileName(): String {
        val saveDir = SmallUtils.context?.externalCacheDir!!.path
        val dir = File(saveDir)
        if (!dir.exists()) {
            dir.mkdir() // 创建文件夹
        }
        //用日期作为文件名，确保唯一性
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
        return saveDir + "/" + formatter.format(date) + ".jpg"
    }
}