# TempAndroid



使用Kotlin构建的项目模板，包含大量常用的第三方库，配置项10+，工具20+

# 配置

1. 配置签名：`signingConfigs`需要你在本地的配置文件`local.properties`中设置打包密钥配置【需自行添加如下】，不然项目构建会报错,这样不管打正式包和debug包，都是一种签名文件，不会出现签名不同包名相同的错误，方便调试。

```keystore.path =D\:\\mykey.jks
keystore.path =D\:\\mykey.jks
keystore.password = 123456
keystore.alias = myalias
keystore.alias_password = 123456
```

2. 配置网络：访问https,避免Anroid9+的手机无法访问http接口，只能访问https接口

3. 配置Title：设置全面屏匹配样式，默认去掉title. `<item name="windowNoTitle">true</item>`

4. 配置Uri：`filepath_data.xml`,Anroid7+获取文件要以FileProvider的方式获取文件Uri

5. 配置方法：配置MultiDex（也就是总方法数超过限制>65536）支持

6. 配置打包：自定义了打包文件名称，以（包名的.后一段+时间+v版本名称+打包方式.apk）来命名

7. 配置dataBinding：开启了dataBinding的支持

8. 配置ViewBinding：获取布局上的控件，避免空指针异常，和dataBinding搭配快速构建MVVM

9. 配置了kapt：kapt插件，以支持dataBinding和第三方注释类

10. 配置屏幕适配：使用[ScreenMatch](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fwildma%2FScreenAdaptation%2Fraw%2Fmaster%2FScreenMatch.jar)插件，具体使用参考：[一种非常好用的Android屏幕适配](https://www.jianshu.com/p/1302ad5a4b04)

    

# 三方库

1. [banner](https://github.com/youth5201314/banner)轮播图
2. [logger](https://github.com/orhanobut/logger)日志：漂亮易定位
3.  [glide](https://github.com/bumptech/glide)图片加载
4. [ViewBindingKtx](https://github.com/DylanCaiCoding/ViewBindingKtx)扩展库：两种方式快速绑定视图
5. [leakcanary](https://github.com/square/leakcanary)：检测内存泄漏
6. [XXPermissions](https://github.com/getActivity/XXPermissions)：权限管理
7. 





# 我的工具

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

20. `DataBindingAdapter`：dataBinding控件属性扩展

    



