package com.smallcake.temp.utils

import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import java.util.regex.Pattern

object EditTextUtils {
    /**
     * 验证多个输入栏未输入，提示为：（"请填写" + 你xml设置的Hint字段）
     * @param editTexts 编辑框
     * @return 双项提示，编辑框警告提示，弹出提示
     * 例：
     * if(EditTextUtils.isEmpty(this,etRname, etNo, etAddr))return;
     */
    fun isEmpty(vararg editTexts: EditText): Boolean {
        for (editText in editTexts) {
            val str = editText.text.toString()
            var hintStr = ""
            try {
                hintStr = editText.hint.toString()
            } catch (e: Exception) {
//                e.printStackTrace()
                lww( "$editText  未设置 android:hint=\"\" 属性")
            }
            if (TextUtils.isEmpty(str)) {
                if (!TextUtils.isEmpty(hintStr)) {
                    editText.error = hintStr
                    ToastUtil.showLong(hintStr)
                } else {
                    editText.error = "未填写"
                    ToastUtil.showLong("有选项未填写")
                }
                return true
            }
        }
        return false
    }

    /**
     * 设置EditText只能输入两位小数 48.88
     * @param editText 编辑框
     */
    fun setTwoDecimal(editText: EditText) {
        editText.inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                cs: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {}
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {}

            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty()) {
                    val temp = editable.toString()
                    val posDot = temp.indexOf(".")
                    if (posDot <= 0) return
                    if (temp.length - posDot - 1 > 2) {
                        editable.delete(posDot + 3, posDot + 4)
                    }
                }
            }
        })
    }

    /**
     * 过滤表情和特殊字符
     * @param et
     */
    fun setEtFilter(et: EditText?) {
        if (et == null) return
        //表情过滤器
        val emojiFilter =
            InputFilter { source, start, end, dest, dstart, dend ->
                val emoji = Pattern.compile(
                    "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
                )
                val emojiMatcher = emoji.matcher(source)
                if (emojiMatcher.find()) {
                    ""
                } else null
            }
        //特殊字符过滤器
        val specialCharFilter =
            InputFilter { source, start, end, dest, dstart, dend ->
                val regexStr =
                    "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
                val pattern = Pattern.compile(regexStr)
                val matcher = pattern.matcher(source.toString())
                if (matcher.matches()) {
                    ""
                } else {
                    null
                }
            }
        et.filters = arrayOf(emojiFilter, specialCharFilter)
    }

    /**
     * 过滤中文
     * @param et
     */
    fun setEtFilterChinese(et: EditText?) {
        if (et == null) return
        //中文过滤器
        val emojiFilter =
            InputFilter { source, start, end, dest, dstart, dend ->
                val emoji =
                    Pattern.compile("[\\u4e00-\\u9fa5]")
                val emojiMatcher = emoji.matcher(source)
                if (emojiMatcher.find()) {
                    ""
                } else null
            }
        et.filters = arrayOf(emojiFilter)
    }
}