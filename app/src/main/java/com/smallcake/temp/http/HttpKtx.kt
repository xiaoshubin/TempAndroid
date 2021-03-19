package com.smallcake.temp.http

import androidx.lifecycle.Lifecycle
import com.lxj.xpopup.impl.LoadingPopupView
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 对于线程切换的扩展,从io线程请求，回到主线程接收结果
 * @receiver Observable<T>
 * @return Observable<T>
 */
fun <T> Observable<T>.im(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}

/**
 * 对回调结果的扩展，只关心成功后success的数据处理
 * 1.如果不想弹出错误：传入fail
 * 2.如果想有加载圈：传入dialog
 * @receiver Observable<T>
 * @param success Function1<[@kotlin.ParameterName] T, Unit>
 * @param fail Function1<[@kotlin.ParameterName] String?, Unit>?
 * @param dialog LoadingPopupView?
 */
fun <T> Observable<T>.sub(success: ((t: T) -> Unit), fail: ((error: String?) -> Unit)? = null,dialog: LoadingPopupView? = null){
   return subscribe(object :OnDataSuccessListener<T>(dialog){
       override fun onSuccess(t: T) {
           success.invoke(t)
       }
       override fun onErrMsg(msg: String) {
           if (fail==null)super.onErrMsg(msg)
           fail?.invoke(msg)
       }
   })
}

/**
 * 在生命周期结束时，取消请求
 * @receiver io.reactivex.Observable<T>
 * @param provider LifecycleProvider<Lifecycle.Event>
 * @return io.reactivex.Observable<T>
 */
fun <T> Observable<T>.bindLife(provider: LifecycleProvider<Lifecycle.Event>): Observable<T> {
    Logger.d("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄")
    return bindUntilEvent(provider,Lifecycle.Event.ON_DESTROY)
}




