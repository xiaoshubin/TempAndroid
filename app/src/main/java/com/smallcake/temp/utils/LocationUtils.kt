package com.smallcake.temp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lxj.xpopup.XPopup
import com.yx.jiading.utils.sizeNull
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
    fun getLocationInfo(activity: Activity, listener: (location:Location)->Unit) {
        //1.获取定位服务
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //2.获取当前位置信息中
        val gpsIsOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) //GPS定位是否打开
        if (gpsIsOpen) {
            startLocation(activity, locationManager, listener)
        } else {
//            AlertDialog.Builder(activity)
//                .setTitle("GPS服务")
//                .setMessage("请打开GPS！")
//                .setPositiveButton("去开启") { dialog, which -> openLocationSet(activity) }
//                .setNegativeButton("取消", null)
//                .show()
        }
    }

    fun isOpenGps(activity: Activity): Boolean {
        //1.获取定位服务
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //2.是否打开了Gps
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
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

    fun openLocationSet(activity: Activity) {
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
            .permission(Permission.ACCESS_FINE_LOCATION,Permission.ACCESS_COARSE_LOCATION)
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
     * 根据定位坐标，获取定位地址结果
     * @param activity Activity
     * @param location Location
     */
    fun getAddress(activity: Activity,location: Location,cb:(Address)->Unit){
        val geocoder = Geocoder(activity,Locale.getDefault())
        try {
            Thread{
                val t1 = System.currentTimeMillis()
                val listAddress = geocoder.getFromLocation(location.latitude, location.longitude,1)
                if (listAddress.sizeNull()>0) {
                    val t2 = System.currentTimeMillis()
                    Log.d(TAG,"${t2-t1}ms后的定位地址：${listAddress[0]}")
                    activity.runOnUiThread{cb(listAddress[0])}
                }
            }.start()
        } catch (e: IOException ) {
            e.printStackTrace()
        }
    }
    fun getAddress(activity: Activity,cb:(Address)->Unit){
        getLocationInfo(activity){
            getAddress(activity,cb)
        }
    }

    /**
     * 去百度地图还是高德地图
     * @param context Context
     * @param cb Function1<Date, Unit>
     */
    fun showToBaiduOrGaoDe(context: Context, lat:Double, lng:Double, keyword:String?) {
        XPopup.Builder(context)
            .asCenterList("选择地图", arrayOf("百度地图", "高德地图")) { i, s ->
                when(i){
                    0-> openBmapNavi(context, lat, lng, keyword ?: "")
                    1-> openAmapNavi(context, lat, lng, keyword)
                }
            }.show()
    }
    /**
     * @param coord_type     坐标类型  允许的值为bd09ll、bd09mc、gcj02、wgs84。
     * bd09ll表示百度经纬度坐标，bd09mc表示百度墨卡托坐标，gcj02表示经过国测局加密的坐标，wgs84表示gps获取的坐标
     * @param mode           导航类型导航模式 可选transit（公交）、 driving（驾车）、 walking（步行）和riding（骑行）.
     * @param src            必选参数，格式为：appName  不传此参数，不保证服务
     * @param context Context
     * @param destinationLat String 目的地维度
     * @param destinationLng String 目的地经度
     * @param coord_type String
     * @param mode String
     * @param src String  例如 andr.baidu.openAPIdemo
     * 参考
     * https://lbsyun.baidu.com/index.php?title=uri/api/android
     */
    fun openBmapNavi(
        context: Context,
        destinationLat: Double,
        destinationLng: Double,
        src: String
    ) {
        try {
            val i1 = Intent()
            i1.data = Uri.parse(
                "baidumap://map/direction?destination=" +
                        destinationLat + "," + destinationLng + "&coord_type=wgs84" +
                        "&mode=driving" + "&src=" + src + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end"
            )
            context.startActivity(i1)
        } catch (e: Exception) {
            showToast("未安装百度地图")
        }
    }
    /**
     * 打开高德地图导航
     * @param context Context
     * @param lat Double
     * @param lng Double
     * @param destinationName String?
     */
    fun openAmapNavi(context: Context, lat: Double, lng: Double, destinationName: String?) {
        try {
            val uriString: String?
            val builder = StringBuilder("amapuri://route/plan?sourceApplication=maxuslife")
            builder.append("&dlat=").append(lat)
                .append("&dlon=").append(lng)
                .append("&dname=").append(destinationName)
                .append("&dev=0")
                .append("&t=0")
            uriString = builder.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setPackage("com.autonavi.minimap")
            intent.data = Uri.parse(uriString)
            context.startActivity(intent)
        } catch (e: Exception) {
            showToast("未安装高德地图")
        }
    }

    /**
     * 1.使用高德搜索功能必须引入对应架包，
     * //高德定位功能
     * implementation 'com.amap.api:location:5.6.2'
     * //高德检索
     * implementation 'com.amap.api:search:8.1.0'
     *
     * 2.并申请KEY并在AndroidManifest.xml中配置
     * <meta-data
     *   android:name="com.amap.api.v2.apikey"
     *   android:value="你申请的Key"/>
     *
     * 3.确保调用SDK任何接口前先调用更新隐私合规updatePrivacyShow、updatePrivacyAgree两个接口并且参数值都为true
     *  AMapLocationClient.updatePrivacyShow(this, true, true)
     *  AMapLocationClient.updatePrivacyAgree(this, true)
     *
     * 最后才是使用：
     * 获取高德地图位置信息
     * RegeocodeAddress.city 市
     * RegeocodeAddress.formatAddress 具体地址
     */
//    fun getAddressByAmap(activity: AppCompatActivity, lat: Double, lon:Double,cb: (RegeocodeAddress) -> Unit){
//        val gs =  GeocodeSearch(activity)
//        gs.setOnGeocodeSearchListener(object :GeocodeSearch.OnGeocodeSearchListener{
//            override fun onRegeocodeSearched(result: RegeocodeResult?, p1: Int) {
//                result?.apply {
//                    activity.runOnUiThread{cb(result.regeocodeAddress)}
//                }
//            }
//            override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
//            }
//        })
//        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
//        val latLonPoint = LatLonPoint(lat, lon)
//        val query = RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.GPS)
//        //异步查询
//        gs.getFromLocationAsyn(query)
//    }
}