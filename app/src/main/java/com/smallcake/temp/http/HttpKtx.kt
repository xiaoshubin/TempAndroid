package com.smallcake.temp.http

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.lxj.xpopup.impl.LoadingPopupView
import com.smallcake.temp.utils.ldd
import com.smallcake.temp.utils.lee
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * 对于线程切换的扩展
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
 * 3.绑定页面生命周期，传入lifecycleOwner
 * @receiver Observable<T>
 * @param success Function1<[@kotlin.ParameterName] T, Unit>
 * @param fail Function1<[@kotlin.ParameterName] String?, Unit>?
 * @param dialog LoadingPopupView?
 */
fun <T> Observable<T>.sub(success: ((t: T) -> Unit), fail: ((error: String?) -> Unit)? = null,dialog: LoadingPopupView? = null,lifecycleOwner: LifecycleOwner?=null){
    var lifecycleOwnerNet:LifecycleOwner?=null
   return subscribe(object :OnDataSuccessListener<T>(dialog){

       override fun onStart() {
           super.onStart()
           //绑定生命周期
           if (lifecycleOwnerNet==null){//判空，避免多次注册
               lifecycleOwnerNet = lifecycleOwner
               lifecycleOwnerNet?.let {
                   it.lifecycle.addObserver(NetLifeObserver(this,it.lifecycle))
               }
           }


       }



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
 * 生命周期观察者
 * 当页面关闭，取消网络请求
 */
class NetLifeObserver<T>(private val disObserver: DisposableObserver<T>, private val lifecycle:Lifecycle) : LifecycleObserver {

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    fun pause() {
        ldd("页面暂停 ====")
    }
    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    fun disConnect(){
        lee("页面关闭，断开连接=====")
        //取消监听
        lifecycle.removeObserver(this)
        disObserver.dispose()
    }



}




