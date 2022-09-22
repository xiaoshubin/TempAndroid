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
 *如何用registerForActivityResult替代onActivityResult
 * 发起并接收
private val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
if (activityResult.resultCode == Activity.RESULT_OK) {
val address = activityResult.data?.getStringExtra("address")
bind.etAddress.setText(address)
}
}
val intent = Intent()
intent.putExtra("address",address)
setResult(RESULT_OK, intent)
finish()

bind.recyclerView.apply {
addItemDecoration(GridItemDecoration())
layoutManager = LinearLayoutManager(context)
adapter  = mAdapter
}


mAdapter.apply {
setEmptyId()
loadMoreModule.setOnLoadMoreListener {
page++
loadData()
}
setOnItemClickListener{adapter,_,postion->
val item = adapter.getItem(postion) as AnnouncementList
goActivity(NoticeInfoActivity::class.java,item.announcementId)
}
}
数据加载更多模板
val list = it.data
mAdapter.apply {
if (list.sizeNull() > 0) {
if (page == 1) setList(list) else addData(list!!)
loadMoreModule.loadMoreComplete()
} else {
if (page == 1) setList(list)
loadMoreModule.loadMoreEnd()
}
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


