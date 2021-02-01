package com.smallcake.temp.api.impl

import com.smallcake.temp.api.MobileApi
import com.smallcake.temp.base.Constant
import com.smallcake.temp.bean.PhoneRespone
import com.smallcake.temp.http.BaseResponse
import com.smallcake.temp.http.im
import io.reactivex.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import retrofit2.Retrofit

class MobileImpl :MobileApi , KoinComponent {
    private val retrofit2 = get<Retrofit>(named("hasUrl")){parametersOf(Constant.BASE_PHONE_URL)}
    private val api: MobileApi = retrofit2.create(MobileApi::class.java)
    override fun mobileGet(mobile: String, key: String): Observable<BaseResponse<PhoneRespone>>
        = api.mobileGet(mobile).im()

}