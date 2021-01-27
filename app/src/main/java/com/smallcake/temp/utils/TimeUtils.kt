package com.smallcake.temp.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    /**
     * 时间戳 转 String类型的精确到时分秒
     * @param time
     * @return
     */
    fun tsToMs(time: Int): String {
        val fm =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val time1000 = time.toString().toLong() * 1000
        return fm.format(time1000)
    }

    /**
     * 时间戳 转 String类型的年月日
     * @param time
     * @return
     */
    fun tsToYMD(time: Int): String {
        val fm =
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val time1000 = time.toString().toLong() * 1000
        return fm.format(time1000)
    }

    /**
     * 获取今天年月日
     * @return 2017-08-14
     */
    val todayDate: String
        get() {
            val date = Date()
            val sdf =
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            return sdf.format(date)
        }

    /**
     * 当前毫秒级时间
     * @return 2017/04/07-11:01:06:109
     */
    val millisecondTime: String
        get() = SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS", Locale.CHINA)
            .format(Date())

    /**
     * 获取当前系统的时间戳
     * @return 1502697135
     */
    val time: Int
        get() = (System.currentTimeMillis() / 1000).toInt()

    fun tsToYMDcn(time: Int): String {
        val fm = SimpleDateFormat("yyyy年MM月dd日")
        val time1000 = time.toString().toLong() * 1000
        return fm.format(time1000)
    }

    fun getTodayAddMonthDate(month: Int): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var now: Date? = null
        try {
            now = sdf.parse(todayDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = now
        calendar.add(Calendar.MONTH, month)
        return sdf.format(calendar.time)
    }

    /**
     * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm"<br></br>
     * 如果获取失败，返回null
     * @return
     */
    val uTCTimeStr: String
        get() {
            val format: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val UTCTimeBuffer = StringBuffer()
            // 1、取得本地时间：
            val cal = Calendar.getInstance()
            // 2、取得时间偏移量：
            val zoneOffset = cal[Calendar.ZONE_OFFSET]
            // 3、取得夏令时差：
            val dstOffset = cal[Calendar.DST_OFFSET]
            // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
            cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset))
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH] + 1
            val day = cal[Calendar.DAY_OF_MONTH]
            val hour = cal[Calendar.HOUR_OF_DAY]
            val minute = cal[Calendar.MINUTE]
            val second = cal[Calendar.SECOND]
            UTCTimeBuffer.append(year).append("-").append(month).append("-").append(day)
            UTCTimeBuffer.append("T").append(hour).append(":").append(minute).append(":")
                .append(second)
            return UTCTimeBuffer.toString()
        }
}