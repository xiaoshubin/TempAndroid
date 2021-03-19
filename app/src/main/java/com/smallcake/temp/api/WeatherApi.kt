package com.smallcake.temp.api

import com.smallcake.temp.bean.WeatherResponse
import com.smallcake.temp.http.BaseResponse
import com.smallcake.temp.http.im
import io.reactivex.Observable
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.http.POST
import retrofit2.http.Query



interface WeatherApi {
    @POST("weather/index")
    fun query(@Query("cityname")cityname: String="重庆",@Query("key")key:String="f60ec0ff4a74cf89ebbfdb16e17b4d9d"): Observable<BaseResponse<WeatherResponse>>
}

@KoinApiExtension
class WeatherImpl:WeatherApi, KoinComponent {
    private val api: WeatherApi by inject()
    override fun query(cityname: String, key: String): Observable<BaseResponse<WeatherResponse>>
            = api.query(cityname,key).im()
}