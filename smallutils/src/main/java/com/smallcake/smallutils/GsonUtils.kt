package com.smallcake.smallutils

import android.util.Log
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

/**
 * Date:2021/6/20 10:11
 * Author:SmallCake
 * Desc:Gson工具类
 **/
object GsonUtils {
    /**
     * 字符串json格式化
     * @param content String
     * @return String
     */
     fun formatJson(content: String): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonParser = JsonParser()
        val jsonElement = jsonParser.parse(content)
        return gson.toJson(jsonElement)
    }

    /**
     * 打印任何对象的json
     * @param param List<Any>?
     */
    fun printList(param:List<Any>?){
        var jsonString = formatJson(Gson().toJson(param))
        Log.i("GsonUtils",jsonString)
    }

    /**
     * 使用google的Gson失败了
     * 使用fastjson把string转HashMap
     * @param str String
     * @return HashMap<String, String>
     */
    fun strToMap(str:String):HashMap<String, String>{
        return JSON.parseObject(str,HashMap::class.java) as HashMap<String, String>
    }
}