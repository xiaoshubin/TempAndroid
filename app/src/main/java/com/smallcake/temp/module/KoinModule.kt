package com.smallcake.temp.module

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
var gson: Gson? = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .serializeNulls()
    .create()
val loggingInterceptor:HttpLoggingInterceptor =  HttpLoggingInterceptor(object :HttpLoggingInterceptor.Logger {
    override fun log(message: String) {

        if (message.startsWith("{\"")){
            formatJson(message)?.let { Log.d(">>>", "  \n$it") }
        }else  Log.d(">>>",message)
    }

}).setLevel(HttpLoggingInterceptor.Level.BODY)
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

/**
 * 将字符串格式化成 JSON 格式
 */
fun formatJson(json: String?): String? {
    if (json == null) {
        return ""
    }
    // 计数tab的个数
    var tabNum = 0
    val builder = StringBuilder()
    val length = json.length
    var last = 0.toChar()
    for (i in 0 until length) {
        val c = json[i]
        if (c == '{') {
            tabNum++
            builder.append(c).append("\n")
                .append(getSpaceOrTab(tabNum))
        } else if (c == '}') {
            tabNum--
            builder.append("\n")
                .append(getSpaceOrTab(tabNum))
                .append(c)
        } else if (c == ',') {
            builder.append(c).append("\n")
                .append(getSpaceOrTab(tabNum))
        } else if (c == ':') {
            if (i > 0 && json[i - 1] == '"') {
                builder.append(c).append(" ")
            } else {
                builder.append(c)
            }
        } else if (c == '[') {
            tabNum++
            val next = json[i + 1]
            if (next == ']') {
                builder.append(c)
            } else {
                builder.append(c).append("\n")
                    .append(getSpaceOrTab(tabNum))
            }
        } else if (c == ']') {
            tabNum--
            if (last == '[') {
                builder.append(c)
            } else {
                builder.append("\n").append(getSpaceOrTab(tabNum)).append(c)
            }
        } else {
            builder.append(c)
        }
        last = c
    }
    return builder.toString()
}

/**
 * 创建对应数量的制表符
 */
fun getSpaceOrTab(tabNum: Int): String? {
    val sb = StringBuffer()
    for (i in 0 until tabNum) {
        sb.append('\t')
    }
    return sb.toString()
}

