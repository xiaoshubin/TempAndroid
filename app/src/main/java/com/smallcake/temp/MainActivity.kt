package com.smallcake.temp

import android.os.Bundle
import com.lsxiao.apollo.core.annotations.Receive
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding


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


    }

    @Receive("event")
    fun event(){
        bind.tvMsg.text = "有消息来袭"
    }



}


