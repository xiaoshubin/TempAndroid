package com.smallcake.smallutils

import android.app.Activity
import android.text.*
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import java.util.regex.Pattern

/**
 * EditText工具类
 */
object EditTextUtils {
    private const val TAG = "EditTextUtils"
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
                Log.w( TAG,"$editText  未设置 android:hint=\"\" 属性")
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

    /**
     * 点击输入文本的搜索后，清空文本，并收起键盘
     * 设置了搜索事件的EditText
     * 注意一定要设置 android:singleLine="true"，才会显示搜索
    <EditText
    android:singleLine="true"
    android:imeOptions="actionSearch"
    android:textColorHint="#CCCCCC"
    android:drawablePadding="8dp"
    android:layout_gravity="center_vertical"
    android:layout_marginLeft="8dp"
    android:layout_marginRight="32dp"
    android:textSize="12sp"
    android:hint="城市中文名称"
    android:paddingLeft="16dp"
    android:drawableLeft="@mipmap/ic_search_gray"
    android:id="@+id/et_search"
    android:background="@drawable/gray_round66_bg"
    android:layout_width="match_parent"
    android:layout_height="34dp"/>
     */
    fun setOnSearch(activity: Activity, et:EditText, cb:(String?)->Unit){
        et.setOnEditorActionListener(object :TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    cb.invoke(v.text.toString())
                    et.setText("")
                    KeyboardUtils.hintKeyboard(activity)
                    return true
                }
                return false
            }
        })
    }
    /**
     * 移动到末尾
     */
    fun setLastLoca(et:EditText){
        et.setSelection(et.text.length)
    }

    /**
     * 显示和隐藏密码
     * @param ivEye ImageView
     * @param etPass EditText
     * @param icEyeOpen Int 显示图标文件
     * @param icEyeClose Int 隐藏图标文件
     */
    fun showAndHidePass(ivEye:ImageView,etPass:EditText,icEyeOpen:Int,icEyeClose:Int){
        var isShowPass = !TextUtils.isEmpty(ivEye.tag as String?)
        ivEye.setOnClickListener {
            if (isShowPass){
                ivEye.setImageResource(icEyeClose)
                isShowPass =false
                etPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }else{
                ivEye.setImageResource(icEyeOpen)
                isShowPass =true
                etPass.inputType = InputType.TYPE_CLASS_TEXT
            }
            // 使光标始终在最后位置
            setLastLoca(etPass)
        }
    }


}

fun EditText.addAfterTextChangeListener(cb:(Editable?)->Unit){
    this.addTextChangedListener(object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            cb.invoke(s)
        }

    })
}
