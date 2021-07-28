package com.smallcake.temp.http

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.smallcake.temp.module.LoadDialog
import com.smallcake.temp.utils.L
import com.smallcake.temp.utils.showToast
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface OnErrorCallback {
    fun onErrMsg(msg: String)
}

abstract class OnDataSuccessListener<T> constructor(dialog: LoadDialog? = null,refreshLayout: SwipeRefreshLayout?) : DisposableObserver<T>(), OnErrorCallback {

    private val loadDialog: LoadDialog? = dialog
    private val ref: SwipeRefreshLayout? = refreshLayout
    private fun showLoading() {
        loadDialog?.show()
        ref?.isRefreshing = true
    }
    private fun hideLoading() {
        loadDialog?.dismiss()
        ref?.isRefreshing = false
    }
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
            when(t.code){
                0  -> onSuccess(t)
                else -> onError(t,RuntimeException("${t.msg}"))
            }
        } else onError(RuntimeException("服务器开小差了！"))

    }



    private fun onError(t:BaseResponse<*>?, e: Throwable) {
        hideLoading()
        if (t!=null&&t.code==-1){
            showToast(t.msg.toString())
            goLogin()
            return
        }
        val message: String = when (e) {
            is SocketTimeoutException -> "网络连接超时：${e.message}"
            is ConnectException -> "网络连接异常：${e.message}"
            is HttpException -> "网络异常：${e.message}"
            is UnknownHostException -> "服务器地址异常：${e.message}"
            is RuntimeException -> "${e.message}"
            else -> e.message ?: "发生异常，但没有可参考的异常信息"
        }
        onErrMsg(message)
    }

    /**
     * 跳登录页面
     */
    private fun goLogin() {
//        val topActivity = ActivityCollector.findTopActivity()
//        if (topActivity==null||topActivity== LoginActivity::class)return
//        val intent= Intent(topActivity, LoginActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        topActivity.startActivity(intent)
    }

    override fun onError(e: Throwable) {
        L.e("实际的异常信息：${e.message}")
        onError(null,java.lang.RuntimeException("网络异常"))
    }
    /**
     * 异常统一处理：弹出异常提示
     */
    override fun onErrMsg(msg: String) {
        showToast(msg)
    }
}