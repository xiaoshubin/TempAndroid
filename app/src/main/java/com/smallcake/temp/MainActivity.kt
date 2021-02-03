package com.smallcake.temp

import android.os.Bundle
import com.lsxiao.apollo.core.annotations.Receive
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.utils.BottomNavUtils
import com.smallcake.temp.utils.ZxingUtils


class MainActivity : BaseBindActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        onEvent()
    }

    private fun onEvent() {

        bind.btnGet2.setOnClickListener{
            goActivity(TestActivity::class.java)
        }

    }


    private fun initView() {

        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)
        val createQRCode = ZxingUtils.createQRCode("你好呀！")
        bind.iv.setImageBitmap(createQRCode)


    }
    @Receive("event")
     fun onEditTxt(){
        bind.tvMsg.text = ("有文字来袭")
    }




}


