package com.smallcake.temp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
abstract class BaseActivity: AppCompatActivity() {
    protected val dataProvider: DataProvider = get()
    protected val dialog:LoadingPopupView by inject{parametersOf(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivity(this)


    }
    override fun onDestroy() {
        super.onDestroy()
        removeActivity(this)
    }


}




