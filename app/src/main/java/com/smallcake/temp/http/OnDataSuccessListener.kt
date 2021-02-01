package com.smallcake.temp.http

import com.bumptech.glide.load.HttpException
import com.lxj.xpopup.impl.LoadingPopupView
import com.smallcake.temp.utils.lee
import com.smallcake.temp.utils.showToast
import io.reactivex.observers.DisposableObserver
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface OnErrorCallback {
    fun onErrMsg(msg: String)
}

abstract class OnDataSuccessListener<T> constructor(dialog: LoadingPopupView? = null) : DisposableObserver<T>(), OnErrorCallback {

    private val loadDialog: LoadingPopupView? = dialog
    private fun showLoading() = loadDialog?.show()
    private fun hideLoading() = loadDialog?.dismiss()
    protected abstract fun onSuccess(t: T)

    override fun onStart() {
        super.onStart()
        showLoading()
    }

    override fun onComplete() {
        hideLoading()
    }

    override fun onNext(t: T) {
        if (t is BaseResponse<*>) {
            if (t.error_code == 0) onSuccess(t)
            else  onError(RuntimeException("[${t.error_code}]${t.reason}"))
        } else onError(RuntimeException("服务器开小差了！"))

    }


    override fun onError(e: Throwable) {
        hideLoading()
        val message: String = when (e) {
            is SocketTimeoutException -> "网络连接超时：${e.message}"
            is ConnectException -> "网络连接异常：${e.message}"
            is HttpException -> "网络异常：${e.message}"
            is UnknownHostException -> "服务器地址异常：${e.message}"
            is RuntimeException -> "数据异常：${e.message}"
            else -> e.message ?: "发生异常，但没有可参考的异常信息"
        }
        lee(message)
        onErrMsg(message)


    }
    /**
     * 异常统一处理：弹出异常提示
     */
    override fun onErrMsg(msg: String) {
        showToast(msg)
    }
}