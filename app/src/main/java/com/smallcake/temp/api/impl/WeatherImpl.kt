package com.smallcake.temp.api.impl

import com.smallcake.temp.api.WeatherApi
import com.smallcake.temp.bean.WeatherResponse
import com.smallcake.temp.http.BaseResponse
import com.smallcake.temp.http.im
import io.reactivex.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit

class WeatherImpl:WeatherApi, KoinComponent {

    private val retrofit: Retrofit by inject()
    private val api: WeatherApi = retrofit.create(WeatherApi::class.java)
    override fun query(cityname: String, key: String): Observable<BaseResponse<WeatherResponse>>
        = api.query(cityname,key).im()

}