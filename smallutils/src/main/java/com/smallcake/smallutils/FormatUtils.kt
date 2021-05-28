package com.smallcake.smallutils

import android.content.Context
import android.text.format.Formatter.formatFileSize
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * Date:2021/5/28
 * Author:SmallCake
 * Desc:单位转换工具类
 **/
object FormatUtils {
    /**
     * trans KB, MB, GB
     *
     * @param sizeBytes long
     * @return 47kb, 4.70M, 1.47GB
     */
    fun formatSize(context: Context, sizeBytes: Long): String? {
        return formatFileSize(context, sizeBytes)
    }

    /**
     * trans 47%
     *
     * @param currentLength
     * @param totalLength
     * @return 47
     */
    fun getProgress(currentLength: Long, totalLength: Long): Int {
        return (currentLength * 100 / totalLength).toInt()
    }

    /**
     * 1,000.00
     *
     * @param num
     * @return
     */
    fun qianweifenge(num: Float): String? {
        val df = DecimalFormat("#,##0.00")
        return df.format(num)
    }

    fun twoDecimal(f: Float): String? {
        val nf: NumberFormat = NumberFormat.getNumberInstance() //建立数字格式化引用
        nf.maximumFractionDigits = 2 //百分比小数点最多2位
        return nf.format(f)
    }
    fun test() {
        val df = DecimalFormat()
        val data = 1234.56789 //格式化之前的数字
        //1、定义要显示的数字的格式（这种方式会四舍五入）
        var style = "0.0"
        df.applyPattern(style)
        println("1-->" + df.format(data)) //1234.6
        //2、在格式后添加诸如单位等字符
        style = "00000.000 kg"
        df.applyPattern(style)
        println("2-->" + df.format(data)) //01234.568 kg
        //3、 模式中的"#"表示如果该位存在字符，则显示字符，如果不存在，则不显示。
        style = "##000.000 kg"
        df.applyPattern(style)
        println("3-->" + df.format(data)) //1234.568 kg
        //4、 模式中的"-"表示输出为负数，要放在最前面
        style = "-000.000"
        df.applyPattern(style)
        println("4-->" + df.format(data)) //-1234.568
        //5、 模式中的","在数字中添加逗号，方便读数字
        style = "-0,000.0#"
        df.applyPattern(style)
        println("5-->" + df.format(data)) //5-->-1,234.57
        //6、模式中的"E"表示输出为指数，"E"之前的字符串是底数的格式，
        // "E"之后的是字符串是指数的格式
        style = "0.00E000"
        df.applyPattern(style)
        println("6-->" + df.format(data)) //6-->1.23E003
        //7、 模式中的"%"表示乘以100并显示为百分数，要放在最后。
        style = "0.00%"
        df.applyPattern(style)
        println("7-->" + df.format(data)) //7-->123456.79%
        //8、 模式中的"\u2030"表示乘以1000并显示为千分数，要放在最后。
        style = "0.00\u2030"
        //在构造函数中设置数字格式
        val df1 = DecimalFormat(style)
        //df.applyPattern(style);
        println("8-->" + df1.format(data)) //8-->1234567.89‰
    }
}