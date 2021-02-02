package com.smallcake.temp

import android.content.Intent
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

        bind.btnGet2.setOnClickListener{
            startActivity(Intent(this,TestActivity::class.java))
        }

    }


    private fun initView() {

        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)



    }




}


