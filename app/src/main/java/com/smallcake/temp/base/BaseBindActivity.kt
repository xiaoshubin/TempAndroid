package com.smallcake.temp.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.inflateBindingWithGeneric


abstract class BaseBindActivity<VB:ViewBinding>: BaseActivity() {
    lateinit var bind: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = inflateBindingWithGeneric(layoutInflater)
        setContentView(bind.root)
    }

}