package com.smallcake.smallutils

import android.widget.Toast


/**
 * 接受CharSequence类型的msg
 * T改为ToastUtil避免和泛型T冲突
 * Toast声明为静态，避免多次点击疯狂弹出
 */
class ToastUtil private constructor() {
    companion object {
        private var toast: Toast? = null
        @JvmStatic
        fun showLong(message: CharSequence) {
            show(message, Toast.LENGTH_LONG)
        }
        @JvmStatic
        fun showShort(message: CharSequence) {
            show(message, Toast.LENGTH_SHORT)
        }

        private fun show(message: CharSequence, duration: Int) {
            try {
                if (toast != null) toast!!.cancel()
                toast = Toast.makeText(SmallUtils.context, message, duration)
                if (toast != null) toast!!.show()
            } catch (ex: Exception) {
            }
        }
    }

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }
}