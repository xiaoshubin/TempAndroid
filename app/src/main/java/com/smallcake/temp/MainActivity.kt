package com.smallcake.temp

import android.os.Bundle
import com.smallcake.temp.base.BaseBindActivity
import com.smallcake.temp.databinding.ActivityMainBinding
import com.smallcake.temp.listener.BottomNavListener
import com.smallcake.temp.utils.BottomNavUtils
import com.smallcake.temp.utils.ldd
import com.smallcake.temp.utils.ljson

class MainActivity : BaseBindActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        onEvent()
    }

    private fun onEvent() {

    }

    private fun initView() {
        BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)
        BottomNavUtils.setListener(BottomNavListener {ldd("index=$it") })
        ljson("    {\"code\":0,\"message\":\"OK\",\"data\":{\"items\":[{\"id\":272,\"startDate\":\"2020-12-17\",\"endDate\":\"2020-12-18\",\"status\":6,\"typeId\":2,\"typeTitle\":\"电工\",\"num\":1,\"employNum\":1,\"address\":\"重庆市城区涪陵区涪陵修长江\",\"day\":2,\"price\":285,\"settleType\":1,\"advanceType\":3,\"isSafe\":0,\"employerId\":66,\"employerPhone\":\"75644456750\"},{\"id\":141,\"startDate\":\"2020-11-11\",\"endDate\":\"2020-11-30\",\"status\":6,\"typeId\":7,\"typeTitle\":\"钢筋工\",\"num\":5,\"employNum\":5,\"address\":\"湖北省武汉市江岸区黄果树\",\"day\":20,\"price\":500,\"settleType\":2,\"advanceType\":3,\"isSafe\":0,\"employerId\":3,\"employerPhone\":\"60000012314\"},{\"id\":76,\"startDate\":\"2020-11-11\",\"endDate\":\"2020-11-15\",\"status\":6,\"typeId\":6,\"typeTitle\":\"抹灰工\",\"num\":1,\"employNum\":1,\"address\":\"重庆市重庆城区涪陵区顺江花园小区\",\"day\":5,\"price\":475,\"settleType\":1,\"advanceType\":3,\"isSafe\":0,\"employerId\":10,\"employerPhone\":\"60000012318\"},{\"id\":72,\"startDate\":\"2020-11-11\",\"endDate\":\"2020-12-01\",\"status\":6,\"typeId\":3,\"typeTitle\":\"木工\",\"num\":10,\"employNum\":10,\"address\":\"重庆市重庆城区大渡口区花岗片麻岩产业园区\",\"day\":21,\"price\":380,\"settleType\":1,\"advanceType\":3,\"isSafe\":0,\"employerId\":10,\"employerPhone\":\"60000012318\"},{\"id\":31,\"startDate\":\"2020-11-10\",\"endDate\":\"2020-11-30\",\"status\":6,\"typeId\":5,\"typeTitle\":\"油漆工\",\"num\":8,\"employNum\":8,\"address\":\"重庆市重庆城区大渡口区重钢集团\",\"day\":21,\"price\":475,\"settleType\":2,\"advanceType\":3,\"isSafe\":0,\"employerId\":2,\"employerPhone\":\"60000012310\"},{\"id\":4,\"startDate\":\"2020-11-10\",\"endDate\":\"2020-11-15\",\"status\":6,\"typeId\":3,\"typeTitle\":\"木工\",\"num\":10,\"employNum\":10,\"address\":\"重庆市重庆城区万州区人民路隧道工程有限公司\",\"day\":6,\"price\":350,\"settleType\":1,\"advanceType\":3,\"isSafe\":0,\"employerId\":2,\"employerPhone\":\"60000012310\"}],\"page\":1,\"totalPage\":1}}\n")
    }



}