# TempAndroid



使用Kotlin构建的项目模板，包含大量常用的第三方库和配置项

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

   

# 三方库

1. 引入[banner](https://github.com/youth5201314/banner)轮播图
2. 引入[logger](https://github.com/orhanobut/logger)日志：漂亮易定位
3. 引入 [glide](https://github.com/bumptech/glide)图片加载

