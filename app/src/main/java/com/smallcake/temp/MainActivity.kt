package com.smallcake.temp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import coil.load
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lxj.xpopup.XPopup
import com.smallcake.smallutils.CameraUtils
import com.smallcake.smallutils.MediaUtils
import com.smallcake.smallutils.ShapeCreator
import com.smallcake.smallutils.TimeUtils
import com.smallcake.smallutils.text.NavigationBar
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.bean.UserBean
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.module.MobileViewModule
import com.smallcake.temp.utils.BottomNavUtils
import com.smallcake.temp.utils.ldd
import com.smallcake.temp.utils.showToast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : BaseBindActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?, bar: NavigationBar) {
        bar.hide()
        initView()
        onEvent()
        checkPermission()
    }

    private fun onEvent() {
        bind.btnGet.setOnClickListener{
            MediaUtils.startRecord(this)
        }
        bind.btnGet2.setOnClickListener{
            MediaUtils.stopRecord()
            ldd("录制的音频路径为："+MediaUtils.filePath)
        }

    }
    private fun checkPermission(){
        XXPermissions.with(this)
            .permission(Permission.RECORD_AUDIO)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all){

                    }
                }
                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never) {
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(this@MainActivity, permissions)
                    } else {
                        showToast("获取定位权限失败")
                    }
                }
            })
    }

    private fun initView() {
        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)
        ShapeCreator.create()
            .setCornerRadius(10f)
            .setSolidColor(Color.LTGRAY)
            .into(bind.tvDesc)

        val module: MobileViewModule by viewModels()
        bind.lifecycleOwner = this
        bind.viewmodel = module

        bind.iv.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1402%2F07%2Fc7%2F31066355_31066355_1391779709500_mthumb.jpg&refer=http%3A%2F%2Fimg.pconline.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620293927&t=acd44048f6ca8548e200395f760fcfb6")


        bind.user = UserBean("xiao",0)

    }




}


