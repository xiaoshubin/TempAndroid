package com.smallcake.temp.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lsxiao.apollo.core.Apollo
import com.lsxiao.apollo.core.contract.ApolloBinder
import com.lxj.xpopup.impl.LoadingPopupView
import com.smallcake.temp.http.DataProvider
import com.smallcake.temp.utils.addActivity
import com.smallcake.temp.utils.removeActivity
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * Activity基类
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val dataProvider: DataProvider = get()//注入数据提供者
    protected val dialog: LoadingPopupView by inject { parametersOf(this) }//注入单例加载圈
    private var mBinder: ApolloBinder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivity(this)
        mBinder = Apollo.bind(this)


    }

    override fun onDestroy() {
        super.onDestroy()
        removeActivity(this)
        mBinder?.unbind()
    }
    //去其他页面，不传参
    fun goActivity(clz: Class<*>) = startActivity(Intent(this, clz))


}




