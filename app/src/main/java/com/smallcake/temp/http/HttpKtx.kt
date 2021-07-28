package com.smallcake.temp.http

import androidx.lifecycle.Lifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.orhanobut.logger.Logger
import com.smallcake.temp.module.LoadDialog
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

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
fun <T> Observable<T>.sub(success: ((t: T) -> Unit), fail: ((error: String?) -> Unit)? = null,dialog: LoadDialog? = null,ref: SwipeRefreshLayout?=null){
    return subscribe(object :OnDataSuccessListener<T>(dialog,ref){
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



fun getImgBody(file: File): MultipartBody.Part{
    val asRequestBody = file.asRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
    return  MultipartBody.Part.createFormData("file", file.name, asRequestBody)
}
fun File.buildPart(): MultipartBody.Part{
    val asRequestBody = this.asRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
    return  MultipartBody.Part.createFormData("file", this.name, asRequestBody)
}

fun JSONObject.build(): RequestBody {
    return this.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}

/**
 * 简化只传递一个参数的清空
 * @receiver String
 * @param key String
 * @param value String
 * @return RequestBody
 */
fun String.put(key:String): RequestBody {
    return JSONObject().put(key,this).build()
}
fun Int.put(key:String): RequestBody {
    return JSONObject().put(key,this).build()
}




