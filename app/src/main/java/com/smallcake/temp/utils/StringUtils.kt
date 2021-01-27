package com.smallcake.temp.utils

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
        val p = Pattern.compile("[a-zA-Z]")
        val m = p.matcher(str)
        return m.matches()
    }

    /**
     * 是否输入了特殊字符
     */
    fun compileExChar(str: String): Boolean {
        val limitEx ="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val pattern = Pattern.compile(limitEx)
        val m = pattern.matcher(str)
        return m.find()
    }
}