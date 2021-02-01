package com.smallcake.temp.api

import com.smallcake.temp.bean.PhoneRespone
import com.smallcake.temp.http.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MobileApi {
    @GET("mobile/get")
    fun mobileGet(@Query("phone")phone: String, @Query("key")key:String="c95c37113391b9fff7854ce0eafe496d"): Observable<BaseResponse<PhoneRespone>>
}