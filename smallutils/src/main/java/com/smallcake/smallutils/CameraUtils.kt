package com.smallcake.smallutils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.smallcake.smallutils.fragment.InvisibleFragment
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
    private const val TAG ="InvisibleFragment"

    /**
     * 系统原生相机拍照
     * 特点：简洁，不关心图片名称，不申请存储权限，因为是保存在应用包名缓存路径中的
     * 目的：拿到一张拍照后的图片地址
     * @param activity 页面
     * @param cb 拍照后的回调
     * @param cameraFacing true:前置摄像头 false:后置摄像头 默认false
     * 注意：需要在AndroidManifest.xml中配置权限<uses-permission android:name="android.permission.CAMERA" />
     * 并在调用的页面申请CAMERA权限
     * 例如使用XXPermissions请求权限后拍照：
     *
         XXPermissions.with(this)
        .permission(Permission.CAMERA)
        .request { permissions, all ->
            if (all){
                CameraUtils.takePhoto(this,{
                ldd("拍照后的地址为：$it")
                },true)
            }
    }
     */
    fun takePhoto(activity: FragmentActivity, cb:(String) -> Unit, cameraFacing: Boolean=false){
        val fragmentManager = activity.supportFragmentManager
        val existedFragment  = fragmentManager.findFragmentByTag(TAG)
        val fragment = if (existedFragment!=null){
            existedFragment as InvisibleFragment
        }else{
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment,TAG).commitNow()
            invisibleFragment
        }
        fragment.takePhotoNow(activity,cb,cameraFacing)
    }
}