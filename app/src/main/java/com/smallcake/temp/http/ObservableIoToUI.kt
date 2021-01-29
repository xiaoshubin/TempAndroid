package com.smallcake.temp.http

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ObservableIoToUI<T> : Observable<T>() {
    init {
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

     override fun subscribeActual(observer: Observer<in T>?) {


     }
 }