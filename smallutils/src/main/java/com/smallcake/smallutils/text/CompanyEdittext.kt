package com.smallcake.smallutils.text

import android.content.Context
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.smallcake.smallutils.R

/**
 * Date: 2020/9/28
 * author: SmallCake
 * 自动追加单位元的小控件
<com.smallcake.smallutils.text.CompanyEdittext
    android:id="@+id/et_price"
    android:layout_width="match_parent"
    android:layout_height="80dp"
    android:background="@color/transparent"
    android:digits="0123456789"
    android:maxLength="5"
    android:gravity="center_vertical"
    android:hint="请填写合理的工价"
    android:inputType="number|textMultiLine"
    android:paddingLeft="16dp"
    android:singleLine="false"
    android:textColor="#000000"
    android:textSize="16sp"
    app:ce_text="元"
    app:ce_text_color="#000000" />
 */
class CompanyEdittext : AppCompatEditText {
    private var mContext: Context
    private var ceText: String? = null //文本内容
    private var ceColor:Int=0x000000 //文字颜色 = 0

    constructor(context: Context) : super(context){
        this.mContext = context
        initView(null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        this.mContext = context
        initView(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.mContext = context
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.CompanyEdittext)
            ceText = array.getString(R.styleable.CompanyEdittext_ce_text)
            ceColor = array.getColor(R.styleable.CompanyEdittext_ce_text_color, 0x000000)
            array.recycle()
        }
        addTextChangedListener(textWatcher)
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            if (ceText!!.trim { it <= ' ' }.isEmpty()) return
            if (!TextUtils.isEmpty(s.toString())) {
                removeTextChangedListener(this) //移除输入监听
                if (s.toString().trim { it <= ' ' } == ceText) {
                    setText("")
                } else {
                    val str = s.toString().replace(ceText!!, "") + ceText //去重
                    //设置文字颜色
                    if (ceColor != 0) {
                        val builder = SpannableStringBuilder(str)
                        builder.setSpan(ForegroundColorSpan(ceColor), str.length - ceText!!.length, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        text = builder
                    } else setText(str)
                }
                addTextChangedListener(this)
            }
        }
    }

    //设置光标位置
    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (text.toString().isNotEmpty() && selEnd == text.toString().length) {
            setSelection(text.toString().length - ceText!!.length)
        } else {
            setSelection(selStart)
        }
    }
}