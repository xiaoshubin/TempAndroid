package com.smallcake.temp

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.lsxiao.apollo.core.Apollo
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityTestBinding
import com.smallcake.temp.http.sub
import com.smallcake.temp.utils.ldd
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class TestActivity : BaseBindActivity<ActivityTestBinding>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val provider: LifecycleProvider<Lifecycle.Event> = AndroidLifecycle.createLifecycleProvider(this)
        bind.btnGet1.setOnClickListener{
            dataProvider.weather.query()
                .bindUntilEvent(provider, Lifecycle.Event.ON_PAUSE)

                .sub({
                    bind.tvMsg.text = it.result.toString()
                },dialog = dialog)

        }

        Observable.timer(5,TimeUnit.SECONDS)
            .bindUntilEvent(provider, Lifecycle.Event.ON_DESTROY)
            .subscribe(object :Observer<Long>{
                override fun onComplete() {
                    ldd("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    ldd("onSubscribe")
                }

                override fun onNext(t: Long) {
                    ldd("onNext")
                    Apollo.emit("event")
                }

                override fun onError(e: Throwable) {
                    ldd("onError")
                }

            })
    }
}


