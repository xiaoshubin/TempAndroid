package com.smallcake.temp.utils

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.smallcake.smallutils.DpUtils
import com.smallcake.smallutils.SmallUtils
import com.smallcake.temp.R
import com.yx.jiading.utils.sizeNull
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView


/**
 * Date:2021/7/28 16:04
 * Author:SmallCake
 * Desc:快速的创建切换Tabs
 **/
object TabUtils {

    fun createTabs(magicIndicator: MagicIndicator,tabs:List<String>,cb:(Int)->Unit){
        val helper =  FragmentContainerHelper(magicIndicator)
        val commonNavigator = CommonNavigator(magicIndicator.context)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return  tabs.sizeNull()
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView? {
                val tv = ColorTransitionPagerTitleView(context)
                tv.apply {
                    textSize = 16f
                    normalColor = Color.GRAY
                    selectedColor = Color.BLACK
                    text = tabs[index]
                    setOnClickListener {
                        helper.handlePageSelected(index)
                        cb.invoke(index)
                    }
                }

                return tv
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                val indicator = LinePagerIndicator(context)
                indicator.apply {
                    yOffset = 30f
                    lineWidth = 60f
                    setColors(ContextCompat.getColor(context, R.color.darkcyan))
                    roundRadius = DpUtils.dp2pxFloat(2f)
                    mode = LinePagerIndicator.MODE_EXACTLY
                }

                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator

    }
    fun initTab(tabLayout: TabLayout, names:List<String>, cb:(Int)->Unit){
        tabLayout.removeAllTabs()
        names.forEachIndexed {i,s->
            val tab = tabLayout.newTab()
            val view  = LayoutInflater.from(SmallUtils.context).inflate(R.layout.tab_nav_bottom_layout,null)
            val tv = view.findViewById<TextView>(R.id.tv)
            tv.text  = s
            tv.setTextColor(Color.parseColor(if (i==0)"#000000" else "#464646"))
            view.findViewById<ImageView>(R.id.iv).visibility = if (i==0) View.VISIBLE else View.INVISIBLE
            tab.customView = view
            tabLayout.addTab(tab)
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                cb(position)
                val tv = tab.customView?.findViewById<TextView>(R.id.tv)
                val iv = tab.customView?.findViewById<ImageView>(R.id.iv)
                tv?.apply {
                    textSize=15f
                    setTextColor(Color.parseColor("#000000"))
                    paint.isFakeBoldText  = true
                }

                iv?.visibility = View.VISIBLE
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tv = tab.customView?.findViewById<TextView>(R.id.tv)
                val iv = tab.customView?.findViewById<ImageView>(R.id.iv)
                tv?.apply {
                    textSize=14f
                    setTextColor(Color.parseColor("#464646"))
                    paint.isFakeBoldText  = false
                }
                iv?.visibility = View.INVISIBLE
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

        })
    }
}