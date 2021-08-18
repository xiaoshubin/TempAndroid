package com.smallcake.smallutils

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
    fun timeToStr(time: Int, timeFormat: String = YYYY_MM_DD): String {
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
    fun today(timeFormat: String = YYYY_MM_DD): String {
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
    fun getAddMonthDateFromNow(addMmonth: Int, timeFormat: String = YYYY_MM_DD): String {
        val sdf = SimpleDateFormat(timeFormat, Locale.CHINA)
        val now = Date()
        val calendar = Calendar.getInstance()
        calendar.time = now
        calendar.add(Calendar.MONTH, addMmonth)
        return sdf.format(calendar.time)
    }

    /**
     * 将字符串转为时间戳
     * @param dateFormat 时间戳格式
     * @param dateTime       时间
     * @return 时间戳(毫秒)
     */
    fun strToDate(dateTime: String, dateFormat: String = YYYY_MM_DD_H24_MM_SS): Int {
        val sdf = SimpleDateFormat(dateFormat, Locale.CHINA)
        var date: Date? = null
        try {
            date = sdf.parse(dateTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val lastData = date?.time?:0L
        return (lastData/1000L).toInt()
    }

    /**
     * 获取日期是星期几
     * @param dateTime       时间
     * @param dateFormat 时间戳格式
     *
     * 注意：dateTime格式要为 yyyy-MM-dd
     */
    fun getDayofWeek(dateTime: String, dateFormat: String = YYYY_MM_DD): String {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat(dateFormat, Locale.CHINA)//这里的格式要和传进来的一样，否则会转换错误
        var date: Date? = null
        try {
            date = sdf.parse(dateTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        cal.time = Date(date?.time ?: 0)
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> "星期日"
            2 -> "星期一"
            3 -> "星期二"
            4 -> "星期三"
            5 -> "星期四"
            6 -> "星期五"
            7 -> "星期六"
            else -> ""
        }
    }
    fun getDayofWeek(date: Date?): String {
        val cal = Calendar.getInstance()
        cal.time = Date(date?.time ?: 0)
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> "星期日"
            2 -> "星期一"
            3 -> "星期二"
            4 -> "星期三"
            5 -> "星期四"
            6 -> "星期五"
            7 -> "星期六"
            else -> ""
        }
    }
    /**
     * 获取几天后的时间戳（秒）
     * @param day 几天后
     */
    fun getTimeDaysLater(day: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + (day * 60 * 60 * 24)) //秒
        return (calendar.time.time/1000L).toInt()
    }

    /**
     * 和现在的时间对比的时间差
     * @param dateTime String
     * @return Int 秒
     */
    fun diffTime(dateTime: String): Int {
        var timeInt=0
        if (dateTime.length==16){
            timeInt = strToDate(dateTime,"yyyy-MM-dd HH:mm")
        }else if (dateTime.length==19){
            timeInt = strToDate(dateTime,"yyyy-MM-dd HH:mm:ss")
        }
        val c = Calendar.getInstance()
        val currentTime = c.timeInMillis/1000
        c.set(Calendar.SECOND,timeInt)
        val newTime = c.timeInMillis/1000
        return (currentTime - newTime).toInt()
    }

    /**
     * 时间转换为1天00：00：00
     * @param timeX Int
     * @return String
     */
    fun timeToDhms(timeX: Int): String {
        val dayUnit = 24 * 60 * 60
        val hourUnit = 60 * 60
        val dayInt = timeX / (dayUnit)
        val hourInt = (timeX - dayInt * dayUnit) / (hourUnit)
        val minutesInt = (timeX - dayInt * dayUnit - hourInt * hourUnit) / 60
        val secoundInt = timeX - dayInt * dayUnit - hourInt * hourUnit - minutesInt * 60

        val dayStr = if (dayInt > 0) "${dayInt}天" else ""
        val hourStr = if (hourInt==0)"00" else (if (hourInt < 9) "0${hourInt}" else "$hourInt")

        val minutesStr = if (minutesInt==0)"00" else(if (minutesInt < 9) "0${minutesInt}" else "$minutesInt")
        val secoundStr = if (secoundInt==0)"00" else(if (secoundInt < 9) "0${secoundInt}" else "$secoundInt")

        return "$dayStr$hourStr:$minutesStr:$secoundStr"
    }


}