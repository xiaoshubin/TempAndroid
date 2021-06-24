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
    const val REQUEST_CAMERA = 909

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

     得到图片的地址后，也许我们还需要压缩，如果是用的鲁班压缩可以
    private fun compressImg(picSavePath:String?) {
        if (TextUtils.isEmpty(picSavePath)) return
        Luban.with(this)
            .load(picSavePath)
            .ignoreBy(200)
            .setTargetDir(externalCacheDir!!.path) //压缩后的图片保存到应用缓存目录
            .setCompressListener(object : OnCompressListener {
            override fun onStart() {}
            override fun onSuccess(file: File) {
                ldd("压缩后的图片大小为：${UnitFormatUtils.formatSize(this@GroupSetActivity,FileUtils.getFileSize(file))}")
            }
            override fun onError(e: Throwable) {}
            })
        .launch()
    }
     */
    fun takePhoto(activity: FragmentActivity, cb:(String?) -> Unit, cameraFacing: Boolean=false){
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