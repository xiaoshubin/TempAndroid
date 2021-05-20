package com.smallcake.temp

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import coil.load
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.smallcake.smallutils.MediaUtils
import com.smallcake.smallutils.ShapeCreator
import com.smallcake.smallutils.text.NavigationBar
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.module.MobileViewModule
import com.smallcake.temp.utils.BottomNavUtils


class MainActivity : BaseBindActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?, bar: NavigationBar) {
        bar.hide()
        initView()
        onEvent()
    }

    private fun onEvent() {
        bind.btnGet.setOnClickListener{
            goActivity(TestActivity::class.java)
        }
        bind.btnGet2.setOnClickListener{
            MediaUtils.playMp3("zltx.mp3",R.raw::class.java)
        }
    }

    private fun initView() {
        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)
        ShapeCreator.create()
            .setCornerRadius(10f)
            .setSolidColor(Color.LTGRAY)
            .into(bind.tvDesc)

        val module: MobileViewModule by viewModels()
        bind.lifecycleOwner = this
        bind.viewmodel = module

        bind.iv.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1402%2F07%2Fc7%2F31066355_31066355_1391779709500_mthumb.jpg&refer=http%3A%2F%2Fimg.pconline.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620293927&t=acd44048f6ca8548e200395f760fcfb6")



    }




}


