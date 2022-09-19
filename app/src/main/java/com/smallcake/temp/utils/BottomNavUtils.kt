package com.smallcake.temp.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smallcake.smallutils.SmallUtils
import com.smallcake.temp.MyApplication
import com.smallcake.temp.R
import com.smallcake.temp.fragment.HomeFragment
import com.smallcake.temp.fragment.ListFragment
import com.smallcake.temp.fragment.MineFragment

/**
 * Date: 2020/8/17
 * author: SmallCake
 * 使用：
val tabs = listOf(
TabNavBottomBean("首页",R.mipmap.icon_selected_tab1,R.mipmap.icon_unselected_tab1),
TabNavBottomBean("列表",R.mipmap.icon_selected_tab2,R.mipmap.icon_unselected_tab2),
TabNavBottomBean("我的",R.mipmap.icon_selected_tab3,R.mipmap.icon_unselected_tab3),
)
val fragments = listOf( HomeFragment(), ListFragment(), MineFragment())
BottomNavUtils.initTabNavi(this,bind.tabLayout,bind.viewPager,tabs,fragments)
 */
object BottomNavUtils {
    /**
     * 动态创建Tab
     * 修改tab_nav_bottom_layout.xml以达到你想要的效果
     * @param activity AppCompatActivity
     * @param tabLayout TabLayout
     * @param viewPager2 ViewPager2
     * @param tabs List<TabNavBottomBean>
     * @param fragments List<Fragment>
     * @param cb Function1<Int, Unit>
     *
     * 如果需求：跳转到对应页面可以使用TabLayout的方法
     * tabLayout.getTabAt(2)?.select()：跳转到第三个页面
     */
    fun initTabNavi(activity:AppCompatActivity,tabLayout: TabLayout, viewPager2: ViewPager2,tabs:List<TabNavBottomBean>,fragments:List<Fragment>, cb:((Int)->Unit)?=null){
        //viewPager2装载Fragment
        viewPager2.adapter = object :FragmentStateAdapter(activity){
            override fun getItemCount(): Int {
                return fragments.size
            }
            override fun createFragment(position: Int): Fragment {
               return fragments[position]
            }
        }
        //最后：tabLayout和ViewPage2联动
        TabLayoutMediator(tabLayout,viewPager2) { tab, position -> tab.text = tabs[position].name }.attach()

        tabLayout.removeAllTabs()
        //动态添加底部自定义导航栏
        tabs.forEachIndexed {i,item->
            val tab = tabLayout.newTab()
            val view  = LayoutInflater.from(SmallUtils.context).inflate(R.layout.tab_nav_bottom_layout,null)
            view.findViewById<TextView>(R.id.tv).text = item.name
            view.findViewById<ImageView>(R.id.iv).setImageResource(if (i==0)item.iconSelect else item.iconUnSelect)
            tab.customView = view
            tabLayout.addTab(tab)
        }
        //tabLayout选中事件
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                cb?.invoke(tab.position)

                tabSelectAnim(tab.customView)
                val position = tab.position
                val tv = tab.customView?.findViewById<TextView>(R.id.tv)
                val iv = tab.customView?.findViewById<ImageView>(R.id.iv)
                tv?.setTextColor(tabs[position].textColorSelect)
                iv?.setImageResource(tabs[position].iconSelect)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                val position = tab.position
                val tv = tab.customView?.findViewById<TextView>(R.id.tv)
                val iv = tab.customView?.findViewById<ImageView>(R.id.iv)
                tv?.setTextColor(tabs[position].textColorUnSelect)
                iv?.setImageResource(tabs[position].iconUnSelect)
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    /**
     * tab被选中后的动画部分
     * @param view View?
     */
    private fun tabSelectAnim(view: View?) {
        val alpha = PropertyValuesHolder.ofFloat("alpha", 0.6f, 1f)
        val pvhX = PropertyValuesHolder.ofFloat("scaleX", 0.9f, 1.1f, 1f)
        val pvhY = PropertyValuesHolder.ofFloat("scaleY", 0.9f, 1.1f, 1f)
        val animator =
            ObjectAnimator.ofPropertyValuesHolder(view, alpha, pvhX, pvhY)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 300
        animator.start()
    }

}

class TabNavBottomBean(val name:String,val iconSelect:Int,val iconUnSelect:Int,val textColorSelect:Int = Color.BLACK,val textColorUnSelect:Int = Color.GRAY)