package com.smallcake.temp

import android.os.Bundle
import com.lsxiao.apollo.core.Apollo
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityTestBinding
import com.smallcake.temp.http.bindActivity
import com.smallcake.temp.http.sub
import com.smallcake.temp.utils.ldd
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class TestActivity : BaseBindActivity<ActivityTestBinding>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind.btnGet1.setOnClickListener{
                dataProvider.weather.query()
                 .bindActivity(provider)
                .sub({
                    bind.tvMsg.text = it.result.toString()
                },dialog = dialog)

        }


        Observable.timer(5, TimeUnit.SECONDS)
            .bindActivity(provider)
            .subscribe(object : Observer<Long> {
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


