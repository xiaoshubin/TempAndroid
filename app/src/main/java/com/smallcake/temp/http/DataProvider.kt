package com.smallcake.temp.http

import com.smallcake.temp.api.MobileImpl
import com.smallcake.temp.api.WeatherImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * 网络数据提供者
 */
class DataProvider :KoinComponent {
    val weather: WeatherImpl = get()
    val mobile: MobileImpl = get()

}