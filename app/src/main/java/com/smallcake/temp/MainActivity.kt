package com.smallcake.temp

import android.R
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Toast
import com.lsxiao.apollo.core.annotations.Receive
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.utils.BottomNavUtils
import es.dmoral.toasty.Toasty


class MainActivity : BaseBindActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        onEvent()
    }


    private fun onEvent() {
        bind.btnGet2.setOnClickListener{queryWeather()}
    }

    fun queryWeather(){
//        dataProvider.weather.query()
//            .bindLife(provider)
//            .sub({bind.item = it.result},dialog = dialog,fail = { ldd("网络有问题")})


        val icon = resources.getDrawable(R.drawable.ic_delete,)
        Toasty.normal(this, "你好啊!", Toast.LENGTH_SHORT, icon).show();
    }


    private fun initView() {
        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)
        Toasty.Config.getInstance()
            .tintIcon(true) // optional (apply textColor also to the icon)
            .setToastTypeface(Typeface.DEFAULT_BOLD) // optional
            .setTextSize(18) // optional
            .allowQueue(false) // optional (prevents several Toastys from queuing)
            .apply(); // required
    }

    @Receive("event")
    fun event()=print("有消息来袭")





}


