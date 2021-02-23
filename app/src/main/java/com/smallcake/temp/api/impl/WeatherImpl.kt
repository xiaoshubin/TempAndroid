package com.smallcake.temp.api.impl

import com.smallcake.temp.api.WeatherApi
import com.smallcake.temp.bean.WeatherResponse
import com.smallcake.temp.http.BaseResponse
import com.smallcake.temp.http.im
import io.reactivex.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WeatherImpl:WeatherApi, KoinComponent {
    private val api: WeatherApi by inject()
    override fun query(cityname: String, key: String): Observable<BaseResponse<WeatherResponse>>
        = api.query(cityname,key).im()

}