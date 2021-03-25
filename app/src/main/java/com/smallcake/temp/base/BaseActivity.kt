package com.smallcake.temp.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.lsxiao.apollo.core.Apollo
import com.lsxiao.apollo.core.contract.ApolloBinder
import com.lxj.xpopup.impl.LoadingPopupView
import com.smallcake.smallutils.ActivityCollector
import com.smallcake.temp.http.DataProvider
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle2.LifecycleProvider
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * Activity基类
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val dataProvider: DataProvider = get()//注入数据提供者
    protected val dialog: LoadingPopupView by inject { parametersOf(this) }//注入单例加载圈
    protected lateinit var provider:LifecycleProvider<Lifecycle.Event>//生命周期提供者
    private var mBinder: ApolloBinder? = null//事件通知者
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
        mBinder = Apollo.bind(this)
        provider = AndroidLifecycle.createLifecycleProvider(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
        mBinder?.unbind()
    }
    //去其他页面，不传参
    fun goActivity(clz: Class<*>) = startActivity(Intent(this, clz))


}




