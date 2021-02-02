package com.smallcake.temp

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityTestBinding
import com.smallcake.temp.http.sub
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindUntilEvent


class TestActivity : BaseBindActivity<ActivityTestBinding>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val provider: LifecycleProvider<Lifecycle.Event> =
            AndroidLifecycle.createLifecycleProvider(this)
        bind.btnGet1.setOnClickListener{
            dataProvider.weather.query()
                .bindUntilEvent(provider, Lifecycle.Event.ON_STOP)
                .sub({
                    bind.tvMsg.text = it.result.toString()
                },dialog = dialog)

        }
    }
}


