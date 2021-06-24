package com.smallcake.temp.utils

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

/**
 * Date:2021/6/24 15:25
 * Author:SmallCake
 * Desc:监听应用前后台
 * 注意：仅在Application中才能使用，因为Application的生命周期能监听到每个Activity
val helper =  AppFrontBackHelper()
helper.register(this,object :AppFrontBackHelper.OnAppStatusListener{
    override fun onFront() {
    }
    override fun onBack() {
    }
})
 */
class AppFrontBackHelper {
    private var mOnAppStatusListener: OnAppStatusListener? = null
    /**
     * 注册状态监听，仅在Application中使用
     * @param application
     * @param listener
     */
    fun register(application: Application, listener: OnAppStatusListener?) {
        mOnAppStatusListener = listener
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }
    fun unRegister(application: Application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }
    private val activityLifecycleCallbacks: ActivityLifecycleCallbacks =
        object : ActivityLifecycleCallbacks {
            //打开的Activity数量统计
            private var activityStartCount = 0
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                activityStartCount++
                //数值从0变到1说明是从后台切到前台
                if (activityStartCount == 1) {
                    //从后台切到前台
                    if (mOnAppStatusListener != null) {
                        mOnAppStatusListener!!.onFront()
                    }
                }
            }
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                activityStartCount--
                //数值从1到0说明是从前台切到后台
                if (activityStartCount == 0) {
                    //从前台切到后台
                    if (mOnAppStatusListener != null) {
                        mOnAppStatusListener!!.onBack()
                    }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        }

    interface OnAppStatusListener {
        fun onFront()
        fun onBack()
    }
}