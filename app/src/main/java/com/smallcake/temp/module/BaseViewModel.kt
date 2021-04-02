package com.smallcake.temp.module

import androidx.lifecycle.ViewModel
import com.smallcake.temp.http.DataProvider
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@KoinApiExtension
open class BaseViewModel: ViewModel(), KoinComponent {
    protected val dataProvider: DataProvider = get()
}