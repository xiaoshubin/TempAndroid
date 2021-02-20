package com.smallcake.temp

import android.os.Bundle
import com.lsxiao.apollo.core.annotations.Receive
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.bean.UserBean
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.http.bindLife
import com.smallcake.temp.http.sub
import com.smallcake.temp.utils.BottomNavUtils
import com.smallcake.temp.utils.ldd


class MainActivity : BaseBindActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind.user = UserBean("Smallcake",8)
        initView()
        onEvent()

    }




    private fun onEvent() {
        bind.btnGet2.setOnClickListener{
//            queryWeather()
            goActivity(TestActivity::class.java)
        }
    }

    fun queryWeather(){
        dataProvider.weather.query()
            .bindLife(provider)
            .sub({bind.item = it.result},dialog = dialog,fail = { ldd("网络有问题")})
    }



    private fun initView() {
        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)

    }

    @Receive("event")
    fun event()= ldd("有消息来袭")





}


