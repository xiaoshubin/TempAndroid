package com.smallcake.temp

import android.os.Bundle
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.bean.WeatherResponse
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.http.BaseResponse
import com.smallcake.temp.utils.BottomNavUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


class MainActivity : BaseBindActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        onEvent()
    }

    private fun onEvent() {
        bind.btnGet.setOnClickListener{
            query()
        }

    }

    private fun query() {
        dataProvider.weather.query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<BaseResponse<WeatherResponse>>() {
                override fun onComplete() {
                    dialog.dismiss()
                }

                override fun onStart() {
                    super.onStart()
                    bind.tvMsg.text = ""
                    dialog.show()

                }

                override fun onNext(t: BaseResponse<WeatherResponse>) {
                    bind.tvMsg.text = t.result.toString()
                }

                override fun onError(e: Throwable) {

                }

            })

    }

    private fun initView() {
        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)



    }




}


