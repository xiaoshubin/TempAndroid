package com.smallcake.temp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lxj.xpopup.XPopup
import com.smallcake.smallutils.ToastUtil.Companion.showLong
import java.io.IOException
import java.util.*


/**
 * Date:2021/6/21 10:15
 * Author:SmallCake
 * Desc:定位工具类
 * 地理编码：根据给定的地名，获得具体的位置信息（比如经度和纬度，以及地址的全称） {@link LocationUtils#getLoactionAddr(Activity, String)}
 * 反地理编码：根据给定的经度和纬度，获取具体的位置信息
 *
 * Android系统定位工具类：
 * LocationManager.GPS_PROVIDER类型得定位不移动不触发，
 * 故本工具类使用LocationManager.NETWORK_PROVIDER的方式进行定位，可根据需要改变定位方式
 *
 * 必要条件
 * 需要AndroidManifest.xml配置权限
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 * 代码中申请权限
 * {@link LocationUtils#requestPermission(Activity)}
 *  并确定开启了网络和gps
 **/
object LocationUtils {
    private const val TAG = "LocationUtils"
    /**
     * 获取定位Location信息
     * @param activity 上下文
     */
    fun getLocationInfo(activity: Activity, listener: (location:Location)->Unit?) {
        //1.获取定位服务
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //2.获取当前位置信息中
        val gpsIsOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) //GPS定位是否打开
        if (gpsIsOpen) {
            startLocation(activity, locationManager, listener)
        } else {
            AlertDialog.Builder(activity)
                .setTitle("GPS服务")
                .setMessage("请打开GPS！")
                .setPositiveButton("去开启") { dialog, which -> openLocationSet(activity) }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    /**
     * 定位查询条件
     * 返回查询条件 ，获取目前设备状态下，最适合的定位方式
     */
    private fun getProvider(loacationManager: LocationManager): String? {
        // 构建位置查询条件
        val criteria = Criteria()
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        //Criteria.ACCURACY_FINE,当使用该值时，在建筑物当中，可能定位不了,建议在对定位要求并不是很高的时候用Criteria.ACCURACY_COARSE，避免定位失败
        // 查询精度：高
        criteria.accuracy = Criteria.ACCURACY_FINE
        // 设置是否要求速度
        criteria.isSpeedRequired = false
        // 是否查询海拨：否
        criteria.isAltitudeRequired = false
        // 是否查询方位角 : 否
        criteria.isBearingRequired = false
        // 是否允许付费：是
        criteria.isCostAllowed = false
        // 电量要求：低
        criteria.powerRequirement = Criteria.POWER_LOW
        // 返回最合适的符合条件的provider，第2个参数为true说明 , 如果只有一个provider是有效的,则返回当前provider
        return loacationManager.getBestProvider(criteria, true)
    }

    private fun openLocationSet(activity: Activity) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivityForResult(intent, 0)
    }


    private fun startLocation(activity: Activity,locationManager: LocationManager,listener: (location:Location)->Unit?) {
        Log.e(TAG,"gps已打开，开始获取定位权限....")
        //为获取地理位置信息时设置查询条件 是按GPS定位还是network定位
        if (ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(activity)
            return
        }
        Log.e(TAG,"已获取定位权限,创建定位配置...")
        val bestProvider = getProvider(locationManager)
        Log.e(TAG,"定位配置：$bestProvider,  开始通过配置获取本地定位对象...")
        //定位方法，第二个参数指的是产生位置改变事件的时间间隔，单位为微秒，第三个参数指的是距离条件，单位为米
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0f,
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    listener.invoke(location)
                    locationManager.removeUpdates(this)//定位成功后移除位置更新
                }
                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            })
    }

    /**
     * 6.0动态申请权限
     */
    private fun requestPermission(activity: Activity) {
        XXPermissions.with(activity)
            .permission(Permission.ACCESS_FINE_LOCATION)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all){

                    }
                }
                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never) {
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(activity, permissions)
                    } else {
                        showToast("获取定位权限失败")
                    }
                }
            })
    }

    /**
     * 地理编码
     * @param activity 上下文
     * @param addrName 地址名称
     * @return
     */
    fun getLoactionAddr(activity: Activity?, addrName: String?): Address? {
        val geoCoder = Geocoder(activity, Locale.getDefault())
        try {
            val addresses: List<Address>? = geoCoder.getFromLocationName(addrName, 5)
            return if (addresses == null || addresses.isEmpty()) null else addresses[0]
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 根据定位坐标，获取定位地址结果
     * @param activity Activity
     * @param location Location
     */
    fun getAddress(activity: Activity,location: Location){
        val address = Geocoder(activity).getFromLocation(location.latitude, location.longitude, 1)[0]
        ldd("定位：${address}")
    }
}