package com.smallcake.temp.utils

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import java.io.*
import java.util.*

/**
 * MMKV存储数据工具类
 *
 * 如何保存一个对象列表？
 * @Parcelize data class Car(val carId: Int,val carNo: String):Parcelable
 * @Parcelize data class CarList(var cardList:List<CardListBean>):Parcelable
 * 保存
 * encodeParcelable("cardList",CarList())
 * 读取
 * val carList:CarList = decodeParcelable("cardList",CarList::calss.java)
 */
object MMKVUtils {
    //You should Call MMKV.initialize() first.
    private val mmkv: MMKV = MMKV.mmkvWithID("small_data")!!

    /**
     * desc:保存对象
     * @param key
     * @param obj
     * @remind 要保存的对象，只能保存实现了serializable的对象
     * 建议使用
     * @see encodeParcelable(key: String?, obj: Parcelable?)
     */
    @java.lang.Deprecated
    fun saveObject(key: String, obj: Any) {
        try {
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            val bos = ByteArrayOutputStream()
            val os = ObjectOutputStream(bos)
            //将对象序列化写入byte缓存
            os.writeObject(obj)
            //将序列化的数据转为16进制保存
            val bytesToHexString = bytesToHexString(bos.toByteArray())
            //保存该16进制数组
            mmkv.putString(key, bytesToHexString)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 保存实现了Parcelable的对象数据
     * @param key String?
     * @param obj Parcelable?
     * @return Boolean
     */
    fun encodeParcelable(key: String?, obj: Parcelable?) = mmkv.encode(key, obj)

    /**
     * 读取对象
     * @param key String
     * @return T
     * 建议使用
     * @see decodeParcelable(key: String?, obj: Parcelable?)
     */
    @java.lang.Deprecated
    fun <T : Any?> readObject(key: String): T? {
        val str = mmkv.getString(key, "")
        str?.let {
            //将16进制的数据转为数组，准备反序列化
            val stringToBytes = stringToBytes(it)
            val bis = ByteArrayInputStream(stringToBytes)
            val ois = ObjectInputStream(bis)
            //返回反序列化得到的对象
            return ois.readObject() as T
        }
        return null
    }

    /**
     * 读取实现了Parcelable的对象数据
     * @param key String?
     * @param tClass Class<T>
     * @return T?
     */
    fun <T : Parcelable?> decodeParcelable(key: String?, tClass:Class<T>): T?
            = mmkv.decodeParcelable(key,tClass)
    /**
     * desc:将数组转为16进制
     * @param bArray
     * @return modified:
     */
    private fun bytesToHexString(bArray: ByteArray): String {
        if (bArray.isEmpty())return ""
        val sb = StringBuffer(bArray.size)
        var sTemp: String
        for (i in bArray.indices) {
            sTemp = Integer.toHexString(0xFF and bArray[i].toInt())
            if (sTemp.length < 2) sb.append(0)
            sb.append(sTemp.toUpperCase(Locale.getDefault()))
        }
        return sb.toString()
    }

    /**
     * 把16进制字符串转换成字节数组 @param hex @return
     */
    private fun stringToBytes(hex: String): ByteArray {
        val len = hex.length / 2
        val result = ByteArray(len)
        val achar = hex.toCharArray()
        for (i in 0 until len) {
            val pos = i * 2
            val toBe1 = toByte(achar[pos])
            val a = toBe1 shl 4
            val b = toByte(achar[pos + 1])
            result[i] = ((a or b).toByte())
        }
        return result
    }

    private fun toByte(c: Char): Int {
        return "0123456789ABCDEF".indexOf(c)
    }

    fun putAny(key: String?, obj: Any) {
        with(mmkv) {
            when (obj) {
                is String -> encode(key, obj)
                is Int -> encode(key, obj)
                is Boolean -> encode(key, obj)
                is Float -> encode(key, obj)
                is Long -> encode(key, obj)
                else -> encode(key, obj.toString())
            }
        }


    }
    fun getAny(key: String?, obj: Any?): Any? {
        return with(mmkv) {
            when (obj) {
                is String -> decodeString(key,obj)
                is Int -> decodeInt(key, obj)
                is Boolean -> decodeBool(key, obj)
                is Float -> decodeFloat(key, obj)
                is Long -> decodeLong(key, obj)
                else -> decodeString(key, obj.toString())
            }
        }
    }


}