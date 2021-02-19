package com.smallcake.temp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.lsxiao.apollo.core.annotations.Receive
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.bean.UserBean
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.http.bindLife
import com.smallcake.temp.http.sub
import com.smallcake.temp.utils.BottomNavUtils
import com.tencent.mmkv.MMKV


class MainActivity : BaseBindActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind.textView.text = "Hello World!"

        initView()
        onEvent()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            val kv = MMKV.defaultMMKV()
            val user = UserBean("SmallCake",8)
            kv?.encode("user",user)
            val decodeParcelable = kv?.decodeParcelable("user", UserBean::class.java)
            bind.textView.text = decodeParcelable.toString()

        },3000)


    }




    private fun onEvent() {
        bind.btnGet2.setOnClickListener{queryWeather()}
    }

    fun queryWeather(){
//        dataProvider.weather.query()
//            .bindLife(provider)
//            .sub({bind.item = it.result},dialog = dialog,fail = { ldd("网络有问题")})
        dataProvider.mobile.mobileGet("18324138218")
            .bindLife(provider)
            .sub({ })

    }


    private fun initView() {
        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)

    }

    @Receive("event")
    fun event()=print("有消息来袭")





}


