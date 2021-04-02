package com.smallcake.temp.module

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smallcake.temp.bean.PhoneRespone
import com.smallcake.temp.http.BaseResponse
import com.smallcake.temp.http.sub
import com.smallcake.temp.utils.showToast

class MobileViewModule : BaseViewModel() {
    companion object{
         var phoneData = MutableLiveData<BaseResponse<PhoneRespone>>()
    }
    val dataLoading: ObservableBoolean = ObservableBoolean(false)

     fun setPhoneData(newPhoneData: BaseResponse<PhoneRespone> ){
         phoneData.value  = newPhoneData
    }
     fun getPhoneData(): LiveData<BaseResponse<PhoneRespone>> {
        return phoneData
    }

    fun getPhoneResponse(mobile: String, key: String): LiveData<BaseResponse<PhoneRespone>> {
        dataLoading.set(true)
        dataProvider.mobile.mobileGet(mobile, key)
            .sub({
                    dataLoading.set(false)
                    phoneData.value = it
            }, fail = {
                it?.let { it1 -> showToast(it1) }
                dataLoading.set(false)
            })
        return phoneData
    }
}