package com.smallcake.temp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.lsxiao.apollo.core.Apollo
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.smallcake.temp.module.appModule
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.core.context.startKoin


/**
 * Date: 2020/1/4
 * author: SmallCake
 */
class MyApplication : Application() {
    companion object{
       lateinit var instance:MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Logger.addLogAdapter(AndroidLogAdapter())
        startKoin{
            modules(appModule)
        }
        Apollo.init(AndroidSchedulers.mainThread(), this);
    }


    //方法数量过多，合并
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}