package com.smallcake.temp

import android.os.Bundle
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.utils.BottomNavUtils


class MainActivity : BaseBindActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        onEvent()
    }

    private fun onEvent() {
        bind.btnGet.setOnClickListener{
            goActivity(TestActivity::class.java)
        }
    }

    private fun initView() {
        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)
        bind.tvDesc.text = "文本内容，超过一行显示折叠图标，否则不显示折叠图标,文本内容，超过一行显示折叠图标，否则不显示折叠图标"
    }


}


