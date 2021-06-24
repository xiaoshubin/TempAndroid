package com.smallcake.temp.utils

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

/**
 * Date:2021/6/24 15:25
 * Author:SmallCake
 * Desc:监听应用前后台
 * 注意：1.仅在Application中才能使用，因为Application的生命周期能监听到每个Activity
 * 2.息屏和亮屏也会调用，息屏：回到后台，亮屏：回到前台
    AppFrontBackHelper().register(this){}
 3.如果想在页面中使用，就在MyApplication中写个监听，来获得此监听
    第一步：MyApplication中声明
    var appStatusListener:((Boolean)->Unit)?=null//监听app前后台
    fun setAppFBListener(listener:((Boolean)->Unit)){
        appStatusListener = listener
    }
    fun unRegistAppFBListener(){
        appStatusListener = null
    }
    第二步：MyApplication的onCreate中写如下代码
    AppFrontBackHelper().register(this){ appStatusListener?.invoke(it) }
    第三步：页面中监听
    MyApplication.instance.setAppFBListener {
        L.i("是否在前台：$it")
    }
    第四步：页面onDestory取消监听
    override fun onDestroy() {
        super.onDestroy()
        MyApplication.instance.unRegistAppFBListener()
    }

 */
class AppFrontBackHelper {
    private var mOnAppStatusListener: ((Boolean)->Unit)?=null

    /**
     * 注册状态监听，仅在Application中使用
     * @param application
     * @param listener
     */
    fun register(application: Application, listener: ((Boolean)->Unit)?) {
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
                    mOnAppStatusListener?.invoke(true)
                }
            }
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                activityStartCount--
                //数值从1到0说明是从前台切到后台
                if (activityStartCount == 0) {
                    //从前台切到后台
                    mOnAppStatusListener?.invoke(false)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        }

}