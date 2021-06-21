# TempAndroid



使用Kotlin构建的项目模板，包含大量常用的第三方库，配置项11，工具23

# 配置项

1. 配置签名：`signingConfigs`  添加了一个smallcake.jks ，替换自己的签名文件
2. 配置网络：访问https,避免Anroid9+的手机无法访问http接口，只能访问https接口

3. 配置导航栏：使用自定义NavigationBar的方式设置通用导航栏

4. 配置Uri：`filepath_data.xml`,Anroid7+获取文件要以FileProvider的方式获取文件Uri

5. 配置多方法：配置MultiDex（也就是总方法数超过限制>65536）支持

6. 配置打包：自定义了打包文件名称，以（包名的.后一段+时间+v版本名称+打包方式.apk）来命名

7. 配置dataBinding：开启了dataBinding的支持，配合`DataBindingAdapter`工具，xml数据一键搞定

8. 配置ViewBinding：获取布局上的控件，空安全，快速构建MVVM

9. 配置了kapt：kapt插件，以支持dataBinding和第三方注释引入

10. 配置屏幕适配：使用[ScreenMatch](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fwildma%2FScreenAdaptation%2Fraw%2Fmaster%2FScreenMatch.jar)插件生成多个尺寸，具体使用参考：[一种非常好用的Android屏幕适配](https://www.jianshu.com/p/1302ad5a4b04)

11. 配置了Parcelable:Parcelable插件，序列化只需要一个注释@Parcelize

    

# 三方库

常用的第三方库

| 库Github                                                     | 说明                                                         |
| :----------------------------------------------------------- | ------------------------------------------------------------ |
| [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper) | 强大而灵活的RecyclerView Adapter                             |
| [retrofit](https://github.com/square/retrofit)（[converter-gson](https://github.com/square/retrofit/tree/master/retrofit-converters/gson/src/main/java/retrofit2/converter/gson)，[RxJava2 Adapter](https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2)），[okhttp](https://github.com/square/okhttp)（[logging-interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)），[gson](https://github.com/google/gson) | 网络                                                         |
| [RxJava](https://github.com/ReactiveX/RxJava)                | Rxjava2，官方已经发布3.0.10了,使用参考[RxJava2-Android-Samples](https://github.com/amitshekhariitbhu/RxJava2-Android-Samples) |
| [XPopup](https://github.com/li-xiaojun/XPopup)               | 非常好用的Pop弹框                                            |
| [banner](https://github.com/youth5201314/banner)             | 轮播图                                                       |
| [logger](https://github.com/orhanobut/logger)                | 日志                                                         |
| [glide](https://github.com/bumptech/glide)                   | 图片加载                                                     |
| [ViewBindingKtx](https://github.com/DylanCaiCoding/ViewBindingKtx) | viewBinding扩展库                                            |
| [leakcanary](https://github.com/square/leakcanary)           | 检测内存泄漏                                                 |
| [XXPermissions](https://github.com/getActivity/XXPermissions) | 权限管理                                                     |
| [koin](https://github.com/InsertKoinIO/koin)                 | 依赖注入https://insert-koin.io/                              |
| [RxLifecycle](https://github.com/trello/RxLifecycle)         | 生命周期绑定，让应用更稳定                                   |
| [Apollo](https://github.com/Sloaix/Apollo)                   | 事件通知，替代EventBus                                       |
| [MMKV](https://github.com/Tencent/MMKV)                      | 用于替代SharedPreferences，性能更优，还可以通过importFromSharedPreferences() 函数，迁移SharedPreferences已有数据 |

可选的第三方库

| 库Github                                                     | 说明                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [ImmersionBar](https://github.com/gyf-dev/ImmersionBar)      | 沉浸式实现（可选）                                           |
| [Luban](https://github.com/Curzibn/Luban)                    | 鲁班压缩，压缩图片（可选）                                   |
| [CalendarView](https://github.com/huanghaibin-dev/CalendarView) | 日历（可选）                                                 |
| [PictureSelector](https://github.com/LuckSiege/PictureSelector) | 多媒体选择器（可选）                                         |
| [EasyFloat](https://github.com/princekin-f/EasyFloat)        | 悬浮球（可选）                                               |
| [ProgressManager](https://github.com/JessYanCoding/ProgressManager) | 图片加载进度，下载进度监听（可选）                           |
| [zxing](https://github.com/zxing/zxing)                      | 二维码生成与扫描（可选），引入地址:`implementation 'com.google.zxing:core:3.3.3'`和`implementation 'com.journeyapps:zxing-android-embedded:3.6.0'`，其中在`utils`中创建了`ZxingUtils`一个工具类，如果不使用zxing记得删除此工具类`ZxingUtils` |
| [Toasty](https://github.com/GrenderG/Toasty)                 | 让Toast不再单调                                              |

kotlin相关的扩展库

| 库名称         | 引入文件                                                |
| -------------- | ------------------------------------------------------- |
| 核心扩展库     | `implementation "androidx.core:core-ktx:1.3.2"`         |
| activity扩展库 | `implementation "androidx.activity:activity-ktx:1.2.2"` |
| fragment扩展库 | `implementation "androidx.fragment:fragment-ktx:1.3.2"` |



# 工具类

1. `ActivityCollector.kt`：管理Activity

2. `BannerUtils.kt`：构建轮播图

3. `BottomNavUtils`：导航绑定ViewPager，并装载Fragment

4. `ComUtils.kt`：一些公共方法，如toast，日志输出...

5. `ToastUtil`：防止疯狂toast

6. `AppUtils`：获取包名，版本，安装APK

7. `ClipboardUtils`：前贴板

8. `DpPxUtils`：dp与px单位转换

9. `EditTextUtils`：EditText判空，限制，过滤

10. `StringUtils`：对字符串的判断

11. `FileUtils`：File文件操作

12. `UnitFormatUtils`：单位格式转换，如Byte转KB, MB, GB

13. `NotificationUtils`：通知工具

14. `ScreenUtils`：获取屏幕宽高，截图等

15. `ShapeCreator`：动态设置share,减少大量shape.xml文件

16. `ShareUtils`：系统分享

17. `SpannableStringUtils`：多媒体文本

18. `TimeUtils`：时间格式转换

19. `AnimUtils`：动画

20. `DataBindingAdapter`：dataBinding控件xml属性扩展

21. `ZxingUtils`：二维码生成

22. `MMKVUtils`：数据存储工具

23. `AppBarUtils`：根据AppBarLayout滚动来逐渐改变控件颜色

    

# 基类

1.BaseActivity：继承AppCompatActivity的基类页面

- 1.1 注入了数据提供者DataProvider（各类网络接口）
- 1.2 注入单例加载圈LoadingPopupView（页面加载进度显示）
- 1.3 创建生命周期提供者LifecycleProvider（生命周期绑定）
- 1.4 创建了事件通知者ApolloBinder（事件通知）
- 1.5 在对应的生命周期中对页面进行管理：addActivity和removeActivity（页面管理）
- 1.6 简化跳页面不传参方法goActivity（页面跳转）

2.BaseBindActivity：继承BaseActivity，加入了ViewBinding的泛型引入

- 子类泛型名称为：布局的驼峰命名+Binding，例如首页布局文件为activity_main.xml，那么泛型实例为ActivityMainBinding，这样就可以直接使用bind.控件了，省略了大量findViewById。同时可以结合DataBind直接绑定对象

  ```kotlin
  class MainActivity : BaseBindActivity<ActivityMainBinding>() {
      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          bind.textView.text = "Hello World!"  
      }
  }
  ```


3.BaseBindFragment：继承androidx.fragment.app.Fragment，加入了ViewBinding的泛型引入，类似BaseBindActivity


# 示例介绍

##### 1.如何请求一个网络数据？

```kotlin
dataProvider//数据提供者
    .weather//那个栏目的数据
    .query()//此栏目查询方法
    .bindLife(provider)//绑定生命周期
    .sub({bind.item = it.result})//只关心请求成功结果
    
```

去掉注释,正规写法,3行一个请求

```kotlin
dataProvider.weather.query()
    .bindLife(provider)
    .sub({bind.item = it.result})
```

1.1加入加载圈？

```kotlin
dataProvider.weather.query()
    .bindLife(provider)
    .sub({bind.item = it.result},dialog = dialog)
```

1.2自己处理失败结果?

```kotlin
dataProvider.weather.query()
    .bindLife(provider)
    .sub({bind.item = it.result},dialog = dialog,fail = { ldd("网络有问题")})
```



##### 2.如何在B页面刷新A页面？

- 首先在B页面写`Apollo.emit("event")`，这样就发出了一个通知

- 然后在A页面写如下代码，这样就接受到B页面发的通知

```kotlin
@Receive("event")
fun event()=print("刷新")
```

##### 3.如何让底部导航栏与页面ViewPager联动?

```kotlin
BottomNavUtils.tabBindViewPager(this,bind.tabLayout,bind.viewPager)
```

##### 4.如何数据绑定对象?

- 首先页面都继承BaseBindActivity，然后布局中layout标签包裹根布局。

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools">
      <data> 
          <variable
              name="user"
              type="com.smallcake.temp.bean.UserBean" />
      </data>
      <LinearLayout
          android:orientation="vertical"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          tools:context=".MainActivity">
          <TextView
          	android:text="@{user.name}"
              android:id="@+id/textView"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content" />
      </LinearLayout>
  </layout>
  ```

- 然后页面中使用

  ```kotlin
  bind.user = UserBean("Smallcake",8)
  ```

  

​	

##### 5 如何添加公共头部

- 在网络注入模块（HttpModule）位置,创建的okHttpClientBuilder后面加入你自己的头部信息

```
    //公共头部拦截器
    val haveHeader = true
    if (haveHeader)okHttpClientBuilder.addInterceptor(Interceptor{
        val request: Request = it.request()
            .newBuilder()
            .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("Accept-Encoding", "gzip, deflate")
            .addHeader("Connection", "keep-alive")
            .addHeader("Accept", "*/*")
            .addHeader("Cookie", "add cookies here")
            .build()
         it.proceed(request)
    })
```

##### 6 如何添加一个新的网络请求分类（推荐按路径功能分类）

- 比如我们要创建一个站点分类Api

1.首先创建一个接口，并实现此接口，kotlin可以把多个文件放一起，方便管理

```kotlin
interface SiteApi {
    //平台联系电话
    @GET("site/plat-phone")
    fun platPhone(): Observable<BaseResponse<PlatPhoneResponse>>
}
class SiteImpl:SiteApi , KoinComponent {
    private val api: SiteApi by inject()
    override fun platPhone(): Observable<BaseResponse<PlatPhoneResponse>> =api.platPhone().im()
}
```

2.在HttpModule模块中添加单例注入

```kotlin
/**
 * 依赖注入module
 */
val appModule = module {
	...
    //网络数据提供者
    single {DataProvider()}
    single {SiteImpl()}
    single {get<Retrofit>().create(SiteApi::class.java)}
}
```

3.最后在DataProvider中注入此接口实现类

```kotlin
/**
 * 网络数据提供者
 */
class DataProvider :KoinComponent {
    val site: SiteImpl = get()
}
```

注意：注入多个不同主机地址域名，需要设置注入时写入不同的name

```kotlin
    //单例retrofit,需要单独定义主机地址
    single (named("hasUrl")){ (url:String?)->
        Retrofit.Builder()
            .baseUrl(url?: Constant.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    single (named("siteUrl")){ (url:String?)->
        Retrofit.Builder()
            .baseUrl(url?: Constant.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
```



##### 7.文件加字段混合上传

​	1.定义接口

```
    @Multipart
    @POST("commonApi/common/demoFile")
    fun demoFile(@PartMap map:HashMap<String, RequestBody> , @Part file:MultipartBody.Part): Observable<BaseResponse<String>>
```

2.实现

```kotlin
        val picSavePath = "/storage/emulated/0/Android/data/com.smallcake.temp/cache/1622801781841719.jpeg"
        val file = File(picSavePath)
        val fileRQ: RequestBody = File(picSavePath).asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val part: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, fileRQ)
        val map = HashMap<String, RequestBody>().apply {
            put("name", "xiao".toRbForm())
            put("phone", "13800138000".toRbForm())
        }
        dataProvider.common.demoFile(map, part)
            .bindLife(provider)
            .sub({})
```

