package com.smallcake.smallutils

import android.content.Context
import android.text.format.Formatter
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * 单位转换工具类
 */
object UnitFormatUtils {
    /**
     * trans KB, MB, GB
     * @param sizeBytes long
     * @return 47kb, 4.70M, 1.47GB
     */
    fun formatSize(context: Context, sizeBytes: Long): String {
        return Formatter.formatFileSize(context, sizeBytes)
    }

    /**
     * trans 47%
     * @param currentLength
     * @param totalLength
     * @return 47
     */
    fun getProgress(currentLength: Long, totalLength: Long): Int {
        return (currentLength * 100 / totalLength).toInt()
    }

    /**
     * 1,000.00
     * @param num
     * @return
     */
    fun qianfen(num: Float): String? {
        val df = DecimalFormat("#,##0.00")
        return df.format(num.toDouble())
    }

    /**
     * 保留两位小数
     * @param f Float
     * @return String?
     */
    fun twoDecimal(f: Float): String {
        val nf = NumberFormat.getNumberInstance() //建立数字格式化引用
        nf.maximumFractionDigits = 2 //百分比小数点最多2位
        return nf.format(f.toDouble())
    }
}