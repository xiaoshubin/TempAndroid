package com.smallcake.smallutils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * 更好的管理Activity
 */
object ActivityCollector{
    var activities: MutableList<AppCompatActivity> = ArrayList()

    fun addActivity(activity: AppCompatActivity) {
        activities.add(activity)
    }

    fun removeActivity(activity: AppCompatActivity) {
        activities.remove(activity)
    }
    /**
     * 关闭Activity
     */
    fun <T : AppCompatActivity?> finishActivity(activity: Class<T>) {
        if ( activities.size > 0) for (a in activities) {
            if (a.javaClass.name == activity.name) a.finish()
        }
    }

    /**
     * 关闭所有Activity
     */
    fun finishAll() {
        for (activity in activities) {
            if (!activity.isFinishing) activity.finish()
        }
    }

    /**
     * 查找一个activity
     * @param activityName String
     * @return AppCompatActivity
     */
    fun findActivity(activityName:String): AppCompatActivity? {
        for (activity in activities) {
            if (!activity.isFinishing&&activity.javaClass.name==activityName){
                return activity
            }
        }
        return null
    }

    /**
     * 获得栈中最顶层的Activity
     * 方便网络框架中跳登录页面
        val topActivity = ActivityCollector.findTopActivity()
        if (topActivity==null||topActivity==LoginActivity::class)return
        val intent= Intent(topActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
        topActivity.startActivity(intent)
     注意：LoginActivity的启动模式应设置为android:launchMode="singleTask"，否则会启动多个LoginActivity页面
     */
    fun findTopActivity(): AppCompatActivity?{
        val am = SmallUtils.context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks: List<ActivityManager.RunningTaskInfo> = am.getRunningTasks(1)
        if (tasks.isNotEmpty()) {
            val mActivityName = tasks[0].topActivity?.className?:"MainActivity"
            return findActivity(mActivityName)
        }
        return null

    }


}