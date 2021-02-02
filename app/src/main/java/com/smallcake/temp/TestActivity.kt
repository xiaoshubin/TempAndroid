package com.smallcake.temp

import android.os.Bundle
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityTestBinding
import com.smallcake.temp.http.sub

class TestActivity : BaseBindActivity<ActivityTestBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind.btnGet1.setOnClickListener{
            dataProvider.weather.query()
                .sub({
                    bind.tvMsg.text = it.result.toString()
                },dialog = dialog,lifecycleOwner = this)
        }
    }
}