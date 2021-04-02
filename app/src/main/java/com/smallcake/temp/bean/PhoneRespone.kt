package com.smallcake.temp.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PhoneRespone(
    var areacode: String,
    val card: String,
    val city: String,
    val company: String,
    val province: String,
    val zip: String
) : Parcelable