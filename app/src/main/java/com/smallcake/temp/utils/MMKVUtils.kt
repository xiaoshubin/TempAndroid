package com.smallcake.temp.utils

import android.annotation.SuppressLint
import com.tencent.mmkv.MMKV
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.io.*
import java.util.*

@KoinApiExtension
object MMKVUtils : KoinComponent {
    private val mmkv: MMKV = get()

    /**
     * desc:保存对象
     * @param key
     * @param obj
     * @remind 要保存的对象，只能保存实现了serializable的对象
     */
    @JvmStatic
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
     * 读取对象
     * @param key String
     * @return T
     */
    @JvmStatic
    fun <T : Any?> readObject(key: String): T? {
        val str = mmkv.getString(key, "")
        str?.let {
            //将16进制的数据转为数组，准备反序列化
            val stringToBytes = StringToBytes(it)
            val bis = ByteArrayInputStream(stringToBytes)
            val ois = ObjectInputStream(bis)
            //返回反序列化得到的对象
            return ois.readObject() as T
        }
        return null
    }

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
    private fun StringToBytes(hex: String): ByteArray {
        val len = hex.length / 2
        val result = ByteArray(len)
        val achar = hex.toCharArray()
        for (i in 0 until len) {
            val pos = i * 2
            val toBe1 = toByte(achar[pos])
            val a = toBe1 shl 4
            val b = toByte(achar[pos + 1])
            val toByte = toByte(achar[pos + 1])
            result[i] = ((a or b).toByte())
        }
        return result
    }

    private fun toByte(c: Char): Int {
        return "0123456789ABCDEF".indexOf(c)
    }

    @SuppressLint("CommitPrefEdits")
    @JvmStatic
    fun putAny(key: String, obj: Any) {
        mmkv.edit().apply() {
            when (obj) {
                is String -> putString(key, obj)
                is Int ->putInt(key, obj)
                is Boolean ->putBoolean(key, obj)
                is Float ->putFloat(key, obj)
                is Long ->putLong(key, obj)
                else ->putString(key, obj.toString())
            }
        }

    }

    @JvmStatic
    fun getAny(key: String, defaultObject: Any): Any? {
        return when (defaultObject) {
            is String -> mmkv.getString(key, defaultObject )
            is Int -> mmkv.getInt(key, (defaultObject ))
            is Boolean -> mmkv.getBoolean(key, (defaultObject))
            is Float -> mmkv.getFloat(key, (defaultObject))
            is Long -> mmkv.getLong(key, (defaultObject))
            else -> null
        }
    }

}