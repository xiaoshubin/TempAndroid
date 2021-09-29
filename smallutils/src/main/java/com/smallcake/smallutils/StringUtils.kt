package com.smallcake.smallutils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object StringUtils {
    /**
     * 是纯数字
     */
    fun isJustNumber(str: String): Boolean {
        val p = Pattern.compile("[0-9]*")
        val m = p.matcher(str)
        return m.matches()
    }

    /**
     * 是纯字母
     */
    fun isJustLetter(str: String): Boolean {
        val p = Pattern.compile("[a-zA-Z]+")
        val m = p.matcher(str)
        return m.matches()
    }

    /**
     * 是否输入了特殊字符
     */
    fun compileExChar(str: String): Boolean {
        val limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val pattern = Pattern.compile(limitEx)
        val m = pattern.matcher(str)
        return m.find()
    }

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr
     * 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    fun IDCardValidate(IDStr: String): Boolean {
        var errorInfo = "" // 记录错误信息
        val ValCodeArr = arrayOf(
            "1", "0", "x", "9", "8", "7", "6", "5", "4",
            "3", "2"
        )
        val Wi = arrayOf(
            "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
            "9", "10", "5", "8", "4", "2"
        )
        var Ai = ""
        // ================号码的长度 15位或18位 ================
        if (IDStr.length != 15 && IDStr.length != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。"
            return false
        }
        // =======================(end)========================  

        // ================ 数字 除最后以为都为数字================
        if (IDStr.length == 18) {
            Ai = IDStr.substring(0, 17)
        } else if (IDStr.length == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15)
        }
        if (isJustNumber(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。"
            return false
        }
        // =======================(end)========================  

        // ================ 出生年月是否有效 ================
        val strYear = Ai.substring(6, 10) //  年份
        val strMonth = Ai.substring(10, 12) // 月份
        val strDay = Ai.substring(12, 14) // 月份
        if (!isDataFormat("$strYear-$strMonth-$strDay")) {
            errorInfo = "身份证生日无效。"
            return false
        }
        val gc = GregorianCalendar()
        val s = SimpleDateFormat("yyyy-MM-dd")
        try {
            if (gc[Calendar.YEAR] - strYear.toInt() > 150
                || gc.time.time - s.parse("$strYear-$strMonth-$strDay").time < 0
            ) {
                errorInfo = "身份证生日不在有效范围。"
                return false
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (strMonth.toInt() > 12 || strMonth.toInt() == 0) {
            errorInfo = "身份证月份无效"
            return false
        }
        if (strDay.toInt() > 31 || strDay.toInt() == 0) {
            errorInfo = "身份证日期无效"
            return false
        }
        // =====================(end)=====================  

        // ================ 地区码时候有效================
        val h: HashMap<String, String> = GetAreaCode()
        if (h[Ai.substring(0, 2)] == null) {
            errorInfo = "身份证地区编码错误。"
            return false
        }
        // ==============================================  

        // ================ 判断最后一位的值================
        var TotalmulAiWi = 0
        for (i in 0..16) {
            TotalmulAiWi = (TotalmulAiWi
                    + Ai[i].toString().toInt() * Wi[i].toInt())
        }
        val modValue = TotalmulAiWi % 11
        val strVerifyCode = ValCodeArr[modValue]
        Ai = Ai + strVerifyCode
        if (IDStr.length == 18) {
            if (Ai == IDStr == false) {
                errorInfo = "身份证无效，不是合法的身份证号码"
                return false
            }
        } else {
            return true
        }
        // =====================(end)=====================  
        return true
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    private fun isDataFormat(str: String): Boolean {
        var flag = false
        val regxStr =
            "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$"
        val pattern1 = Pattern.compile(regxStr)
        val isNo = pattern1.matcher(str)
        if (isNo.matches()) {
            flag = true
        }
        return flag
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private fun GetAreaCode(): HashMap<String, String> = hashMapOf(
        "11" to "北京",
        "12" to "天津",
        "13" to "河北",
        "14" to "山西",
        "15" to "内蒙古",
        "21" to "辽宁",
        "22" to "吉林",
        "23" to "黑龙江",
        "31" to "上海",
        "32" to "江苏",
        "33" to "浙江",
        "34" to "安徽",
        "35" to "福建",
        "36" to "江西",
        "37" to "山东",
        "41" to "河南",
        "42" to "湖北",
        "43" to "湖南",
        "44" to "广东",
        "45" to "广西",
        "46" to "海南",
        "50" to "重庆",
        "51" to "四川",
        "52" to "贵州",
        "53" to "云南",
        "54" to "西藏",
        "61" to "陕西",
        "62" to "甘肃",
        "63" to "青海",
        "64" to "宁夏",
        "65" to "新疆",
        "71" to "台湾",
        "81" to "香港",
        "82" to "澳门",
        "91" to "国外"
    )

    /**
     * 描述：是否是邮箱.
     * @param str 指定的字符串
     * @return 是否是邮箱:是为true，否则false
     */
    fun isEmail(str: String): Boolean? {
        var isEmail = false
        val expr = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
        if (str.matches(Regex(expr))) {
            isEmail = true
        }
        return isEmail
    }

    /**
     * 是否是空
     */
    fun isNull(content: String?): Boolean {
        return content == null || content.isEmpty() || content.toLowerCase(Locale.ROOT) == "null"
    }


}