package com.smallcake.temp.module

import android.content.Context
import com.lxj.xpopup.XPopup
import com.smallcake.temp.api.impl.MobileImpl
import com.smallcake.temp.api.impl.WeatherImpl
import com.smallcake.temp.base.Constant
import com.smallcake.temp.http.DataProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * 依赖注入module
 */
val loggingInterceptor:HttpLoggingInterceptor =  HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
var okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()
val appModule = module {
    //单例，加载圈圈
    single { (context:Context) -> XPopup.Builder(context).asLoading().setTitle("加载中...") }
    //单例，retrofit
//    single {
//         Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .client(okHttpClient)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
    single (named("hasUrl")){ (url:String?)->
         Retrofit.Builder()
            .baseUrl(url?: Constant.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single {
         Retrofit.Builder()
            .baseUrl( Constant.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //网络数据提供者
    single {DataProvider()}
    single {WeatherImpl()}
    single {MobileImpl()}

}


