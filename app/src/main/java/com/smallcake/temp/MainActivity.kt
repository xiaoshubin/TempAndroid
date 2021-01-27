package com.smallcake.temp

import android.os.Bundle
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.utils.BottomNavUtils
import com.smallcake.temp.utils.DpPxUtils
import com.smallcake.temp.utils.EditTextUtils
import com.smallcake.temp.utils.ldd

class MainActivity : BaseBindActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        onEvent()
    }

    private fun onEvent() {
        ldd("100dp==${DpPxUtils.dp2px(100f)}")
        EditTextUtils.setTwoDecimal(bind.et1)
    }

    private fun initView() {
        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)

    }



}