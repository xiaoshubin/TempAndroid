package com.smallcake.smallutils

import android.content.res.AssetManager
import java.io.InputStream

/**
 * Date:2021/6/28 14:34
 * Author:SmallCake
 * Desc: Assets资源管理类
 **/
object AssetsUtils {
    /**
     * 打开assets目录下的资源文件
     * 例如打开assest下的sticker文件夹下的ajmid下的ajmd001.png图片
     * openToInputStream(assestManager,"sticker/ajmid/ajmd001.png")
     * @param assestManager AssetManager 资源管理
     * @param path AssetManager 文件路径
     */
    fun openToInputStream(assestManager: AssetManager,path:String):InputStream{
       return assestManager.open(path)
    }
}