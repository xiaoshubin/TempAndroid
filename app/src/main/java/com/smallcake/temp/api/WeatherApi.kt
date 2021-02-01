package com.smallcake.temp.api

import com.smallcake.temp.bean.WeatherResponse
import com.smallcake.temp.http.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query



interface WeatherApi {
    @GET("weather/index")
    fun query(@Query("cityname")cityname: String="重庆",@Query("key")key:String="f60ec0ff4a74cf89ebbfdb16e17b4d9d"): Observable<BaseResponse<WeatherResponse>>
}