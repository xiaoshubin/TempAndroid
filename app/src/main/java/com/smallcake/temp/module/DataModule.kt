package com.smallcake.temp.module

import com.tencent.mmkv.MMKV
import org.koin.dsl.module

/**
 * 数据存储相关模块
 */
val dataModule = module {
    single {
         MMKV.mmkvWithID("share_data")
    }
}