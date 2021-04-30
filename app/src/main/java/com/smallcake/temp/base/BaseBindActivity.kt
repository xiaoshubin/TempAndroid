package com.smallcake.temp.base

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.inflateBindingWithGeneric
import com.smallcake.smallutils.ResourceUtils
import com.smallcake.smallutils.text.NavigationBar
import com.smallcake.temp.HttpDebug
import com.smallcake.temp.R
import com.smallcake.temp.utils.ldd


abstract class BaseBindActivity<VB:ViewBinding>: BaseActivity() {
    lateinit var bind: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = inflateBindingWithGeneric(layoutInflater)
        setContentView(bind.root)
        //导航栏设置
        val bar = NavigationBar(this)
        bar.setBackgroundColor(Color.WHITE,true)
        bar.backImageView?.setImageResource(R.mipmap.ic_back)
        bar.backImageView?.setOnClickListener{finish()}
        onCreate(savedInstanceState,bar)
        //调试器
        HttpDebug(this, bind.root.parent as ViewGroup)

    }
    abstract fun onCreate(savedInstanceState: Bundle?,bar:NavigationBar)

}