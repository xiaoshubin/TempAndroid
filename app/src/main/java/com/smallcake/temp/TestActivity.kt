package com.smallcake.temp

import android.os.Bundle
import androidx.activity.viewModels
import com.smallcake.smallutils.text.NavigationBar
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityTestBinding
import com.smallcake.temp.module.MobileViewModule


class TestActivity : BaseBindActivity<ActivityTestBinding>() {


    override fun onCreate(savedInstanceState: Bundle?,bar: NavigationBar) {

        val mobileViewModule:MobileViewModule by viewModels()
        bind.lifecycleOwner = this
        bind.viewmodel = mobileViewModule
        bind.btnGet1.setOnClickListener {
            mobileViewModule.getPhoneResponse("18324138218","c95c37113391b9fff7854ce0eafe496d")
        }
        bind.refreshLayout.setOnRefreshListener {
            mobileViewModule.getPhoneResponse("18324138218","c95c37113391b9fff7854ce0eafe496d")
        }



    }


}


