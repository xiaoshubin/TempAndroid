package com.smallcake.temp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smallcake.temp.utils.addActivity
import com.smallcake.temp.utils.removeActivity

/**
 * Activity基类
 */
abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivity(this)


    }
    override fun onDestroy() {
        super.onDestroy()
        removeActivity(this)
    }


}




