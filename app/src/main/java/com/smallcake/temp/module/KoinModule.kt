package com.smallcake.temp.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lxj.xpopup.XPopup
import com.smallcake.temp.api.impl.MobileImpl
import com.smallcake.temp.api.impl.WeatherImpl
import com.smallcake.temp.base.Constant
import com.smallcake.temp.http.DataProvider
import com.smallcake.temp.utils.ldd
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
var gson: Gson? = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .serializeNulls()
    .create()
val loggingInterceptor:HttpLoggingInterceptor =  HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
    override fun log(message: String) {
        ldd(message)
    }
}).setLevel(HttpLoggingInterceptor.Level.BASIC)
var okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()
val appModule = module {
    //单例，加载圈圈
    single { (context:Context) -> XPopup.Builder(context).asLoading().setTitle("加载中...") }
    //单例，retrofit
    single (named("hasUrl")){ (url:String?)->
         Retrofit.Builder()
            .baseUrl(url?: Constant.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    single {
         Retrofit.Builder()
            .baseUrl( Constant.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    //网络数据提供者
    single {DataProvider()}
    single {WeatherImpl()}
    single {MobileImpl()}

}


