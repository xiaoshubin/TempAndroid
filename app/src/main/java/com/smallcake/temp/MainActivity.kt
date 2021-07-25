package com.smallcake.temp

import android.os.Bundle
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.smallcake.smallutils.text.NavigationBar
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.utils.BottomNavUtils
import com.smallcake.temp.utils.showToast
import java.util.*


class MainActivity : BaseBindActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?, bar: NavigationBar) {
        bar.hide()
        initView()
        onEvent()
        checkPermission()
    }

    private fun onEvent() {


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

    }




}


