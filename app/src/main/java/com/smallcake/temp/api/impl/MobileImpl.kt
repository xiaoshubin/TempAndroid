package com.smallcake.temp.api.impl

import com.smallcake.temp.api.MobileApi
import com.smallcake.temp.bean.PhoneRespone
import com.smallcake.temp.http.BaseResponse
import com.smallcake.temp.http.im
import io.reactivex.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MobileImpl :MobileApi , KoinComponent {
    private val api: MobileApi by inject()
    override fun mobileGet(mobile: String, key: String): Observable<BaseResponse<PhoneRespone>>
        = api.mobileGet(mobile).im()

}