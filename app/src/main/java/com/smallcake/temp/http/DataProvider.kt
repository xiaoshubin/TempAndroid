package com.smallcake.temp.http

import com.smallcake.temp.api.WeatherApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import retrofit2.Retrofit

/**
 * 网络数据提供者
 */
class DataProvider :KoinComponent {
    private val retrofit:Retrofit = get()
    val weather: WeatherApi = retrofit.create(WeatherApi::class.java)
}