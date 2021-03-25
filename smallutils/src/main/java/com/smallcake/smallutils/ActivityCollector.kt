package com.smallcake.smallutils

import android.app.Activity
import java.util.*

/**
 * 更好的管理Activity
 */
object ActivityCollector{
    var activities: MutableList<Activity>? = ArrayList()

    fun addActivity(activity: Activity) {
        activities!!.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities!!.remove(activity)
    }

    fun <T : Activity?> finishActivity(activity: Class<T>) {
        if (activities != null && activities!!.size > 0) for (a in activities!!) {
            if (a.javaClass.name == activity.name) a.finish()
        }
    }

    fun finishAll() {
        for (activity in activities!!) {
            if (!activity.isFinishing) activity.finish()
        }
    }
}