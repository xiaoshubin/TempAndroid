package com.smallcake.temp

import android.os.Bundle
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.base.addFragment
import com.smallcake.temp.base.replaceFragment
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.fragment.HomeFragment
import com.smallcake.temp.fragment.ListFragment
import com.smallcake.temp.fragment.MineFragment

class MainActivity : BaseBindActivity<ActivityMainBinding>() {
    private val tag = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        onEvent()
    }

    private fun onEvent() {
        bind.tvHome.setOnClickListener{replaceFragment(HomeFragment(),R.id.content_fragment)}
        bind.tvList.setOnClickListener{replaceFragment(ListFragment(),R.id.content_fragment)}
        bind.tvMine.setOnClickListener{replaceFragment(MineFragment(),R.id.content_fragment)}
    }

    private fun initView() {
        addFragment(HomeFragment(),R.id.content_fragment)
    }

}