package com.smallcake.temp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import com.smallcake.smallutils.ShapeCreator
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.module.MobileViewModule
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
        bind.btnGet2.setOnClickListener{
        }
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


    }


}


