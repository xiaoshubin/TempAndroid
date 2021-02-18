package com.smallcake.temp.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserBean(val name:String,val id:Int):Parcelable