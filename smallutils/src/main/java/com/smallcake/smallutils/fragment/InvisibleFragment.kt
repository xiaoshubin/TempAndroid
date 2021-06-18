package com.smallcake.smallutils.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.smallcake.smallutils.CameraUtils
import com.smallcake.smallutils.SmallUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class InvisibleFragment : Fragment() {
    private var callback:((String?) -> Unit)?=null
    private val picSavePath = getFileName()


    fun takePhotoNow(activity: FragmentActivity, cb:(String?) -> Unit,cameraFacing: Boolean=false) {
        callback=cb
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraFacing)intent.putExtra("android.intent.extras.CAMERA_FACING", 1)//调用前置摄像头
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val uriForFile: Uri =
                FileProvider.getUriForFile(activity, "${activity.packageName}.fileprovider", File(picSavePath))
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile)
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(picSavePath)))
        }
        startActivityForResult(intent, CameraUtils.REQUEST_CAMERA)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode ==CameraUtils. REQUEST_CAMERA) {
            Log.i("图片地址","picSavePath:$picSavePath")
            callback?.invoke(picSavePath)
        }else{
            callback?.invoke(null)
        }
    }
}

