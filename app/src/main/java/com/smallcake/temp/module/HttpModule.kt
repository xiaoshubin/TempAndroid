package com.smallcake.temp.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lxj.xpopup.XPopup
import com.smallcake.temp.api.MobileApi
import com.smallcake.temp.api.MobileImpl
import com.smallcake.temp.api.WeatherApi
import com.smallcake.temp.api.WeatherImpl
import com.smallcake.temp.base.Constant
import com.smallcake.temp.http.DataProvider
import com.smallcake.temp.http.HttpLogInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 网络请求依赖注入module
 */
val httpModule = module {
    //网络数据json格式化
    var gson: Gson? = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .serializeNulls()
        .create()

    //网络请求okhttp客户端
    var okHttpClientBuilder: OkHttpClient.Builder =OkHttpClient.Builder()
    okHttpClientBuilder.addInterceptor(HttpLogInterceptor())//日志打印拦截器


    //公共头部拦截器
    val haveHeader = false
    if (haveHeader)okHttpClientBuilder.addInterceptor(Interceptor{
        val request: Request = it.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer")
            .build()
         it.proceed(request)
    })

    val okHttpClient = okHttpClientBuilder.build()


    //单例，加载圈圈
    single { (context:Context) -> XPopup.Builder(context).asLoading().setTitle("加载中...") }
    //单例retrofit,需要单独定义主机地址
    single (named("hasUrl")){ (url:String?)->
        Retrofit.Builder()
            .baseUrl(url?: Constant.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    single (named("siteUrl")){ (url:String?)->
        Retrofit.Builder()
            .baseUrl(url?: Constant.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    //单例retrofit
    single {
         Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    //网络数据提供者
    single {DataProvider()}
    single { WeatherImpl() }
    single { MobileImpl() }
    single {get<Retrofit>().create(WeatherApi::class.java)}
    single {get<Retrofit>(named("hasUrl")){parametersOf(Constant.BASE_PHONE_URL)}.create(MobileApi::class.java)}

}




