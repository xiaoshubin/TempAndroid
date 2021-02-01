package com.smallcake.temp

import android.os.Bundle
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.http.sub
import com.smallcake.temp.utils.BottomNavUtils


class MainActivity : BaseBindActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        onEvent()
    }

    private fun onEvent() {
        bind.btnGet1.setOnClickListener{
            dataProvider.weather.query()
            .sub({
                bind.tvMsg.text = it.result.toString()
            },dialog = dialog)
        }
        bind.btnGet2.setOnClickListener{
            dataProvider.mobile.mobileGet("13800138000")
                .sub({
                    bind.tvMsg.text = it.result.toString()
                },dialog = dialog)
        }

    }


    private fun initView() {

        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)



    }




}


