package com.smallcake.smallutils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间工具类
 * 12小时 - 时：分：秒  HH改为hh
 * H12_MM_SS = "hh:mm:ss"
 */
object TimeUtils {
    /**
     * 年-月-日
     */
    const val YYYY_MM_DD = "yyyy-MM-dd"
    /**
     * 24小时 - 时：分：秒
     */
    const val H24_MM_SS = "HH:mm:ss"
    /**
     * 12小时 - 时：分：秒
     */
    const val H12_MM_SS = "hh:mm:ss"

    /**
     * 24小时，年-月-日 时：分：秒
     */
    const val YYYY_MM_DD_H24_MM_SS = "yyyy-MM-dd HH:mm:ss"
    /**
     * 24小时，年-月-日 时：分：秒 : 毫秒
     */
    const val YYYY_MM_DD_H24_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS"

    /**
     * 时间戳 转 String
     * @param time 时间戳
     * @param timeFormat 时间戳 默认yyyy-MM-dd
     * @return
     */
    fun timeToStr(time: Int,timeFormat:String= YYYY_MM_DD): String {
        val fm =
            SimpleDateFormat(timeFormat, Locale.CHINA)
        val time1000 = time.toString().toLong() * 1000
        return fm.format(time1000)
    }

    /**
     * 获取今天年月日
     * @param timeFormat 时间戳 默认yyyy-MM-dd
     * @return 2017-08-14
     */
     fun today(timeFormat:String= YYYY_MM_DD): String{
        val date = Date()
        val sdf = SimpleDateFormat(timeFormat, Locale.CHINA)
        return sdf.format(date)
     }


    /**
     * 获取当前系统的时间戳(10位)
     * @return 1502697135
     */
    val currentTime: Int
      get() = (System.currentTimeMillis() / 1000).toInt()
    /**
     * 获取当前系统的时间戳(13位) 毫秒级
     * @return 1502697135000
     */
    val currentTimeSSS: Long
      get() = System.currentTimeMillis() / 1000L

    /**
     * 获取几个月后的今天
     * 例如今天是6月12日（2021-06-12），那么如果addMmonth传入2后就是（2021-08-12）
     * @param addMmonth Int 追加的月份
     * @return String 2021-08-12
     */
    fun getAddMonthDateFromNow(addMmonth: Int, timeFormat:String= YYYY_MM_DD): String {
        val sdf = SimpleDateFormat(timeFormat, Locale.CHINA)
        val now = Date()
        val calendar = Calendar.getInstance()
        calendar.time = now
        calendar.add(Calendar.MONTH, addMmonth)
        return sdf.format(calendar.time)
    }


}