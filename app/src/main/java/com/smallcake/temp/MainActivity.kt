package com.smallcake.temp

import android.os.Bundle
import android.os.Handler
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.smallcake.smallutils.text.NavigationBar
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.fragment.HomeFragment
import com.smallcake.temp.fragment.ListFragment
import com.smallcake.temp.fragment.MineFragment
import com.smallcake.temp.utils.BottomNavUtils
import com.smallcake.temp.utils.TabNavBottomBean
import com.smallcake.temp.utils.showToast
import java.util.*

/**
 * 申请权限
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
 */
class MainActivity : BaseBindActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?, bar: NavigationBar) {
        bar.hide()
        initView()
    }
    private fun initView() {
        val tabs = listOf(
            TabNavBottomBean("首页",R.mipmap.icon_selected_tab1,R.mipmap.icon_unselected_tab1),
            TabNavBottomBean("列表",R.mipmap.icon_selected_tab2,R.mipmap.icon_unselected_tab2),
            TabNavBottomBean("我的",R.mipmap.icon_selected_tab3,R.mipmap.icon_unselected_tab3),
        )
        val fragments = listOf( HomeFragment(), ListFragment(), MineFragment())
        BottomNavUtils.initTabNavi(this,bind.tabLayout,bind.viewPager,tabs,fragments)
    }

}


