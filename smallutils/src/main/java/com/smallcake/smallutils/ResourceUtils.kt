package com.smallcake.smallutils

import android.content.Context
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 */
object ResourceUtils {
    /**
     * 日志标识
     */
    private const val TAG = "ResourceUtils"

    /**
     * 获取应用包下缓存路径
     * @param context 上下文
     * @return 当前
     */
    fun getExternalCacheDir(context: Context): String {
        return context.externalCacheDir!!.path
    }

    /**
     * 创建文件
     * create file
     *
     * @param context 上下文对象
     * @param dir     文件夹名称
     * @param name    文件名称
     * @return
     */
    fun createFile(
        context: Context,
        dir: String,
        name: String?
    ): File? {
        if (name == null) {
            Log.w(TAG, "Please check you file name.")
            return null
        }
        makeDirs(context, dir)
        val file =
            File(getExternalCacheDir(context) + File.separator + dir + File.separator + name)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }

    /**
     * 创建新文件夹
     *
     * @param context 上下文对象
     * @param dir     文件夹名称
     * @return
     */
    fun makeDirs(context: Context, dir: String): String {
        val folder =
            File(getExternalCacheDir(context) + File.separator + dir)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return folder.absolutePath
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param file 将要删除的文件目录
     */
    fun delete(file: File): Boolean {
        if (file.isDirectory) {
            val children = file.list()
            //递归删除目录中的子目录下
            for (i in children.indices) {
                val success =
                    delete(File(file, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        // 目录此时为空，可以删除
        return file.delete()
    }

    /**
     * 计算文件大小
     *
     * @param file 文件夹
     * @return
     */
    fun length(file: File): Long {
        //判断文件是否存在
        return if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory) {
                val children = file.listFiles()
                var size = 0L
                for (content in children) size += length(content)
                size
            } else { //如果是文件则直接返回其大小,以“Kb”为单位
                Log.i(TAG, " length " + file.length() + "kb")
                file.length() / 1024
            }
        } else {
            Log.i(
                TAG,
                "The file or dir does not exist, please check the path is correct!"
            )
            0
        }
    }

    /**
     * 获取文件后缀
     *
     * @param path 路径
     * @return
     */
    fun getSuffix(path: String?): String? {
        return getSuffix(File(path))
    }

    /**
     * 获取文件后缀
     *
     * @param file 文件
     * @return
     */
    fun getSuffix(file: File?): String? {
        if (file == null || !file.exists() || file.isDirectory) {
            return null
        }
        val fileName = file.name
        if (fileName == "" || fileName.endsWith(".")) {
            return null
        }
        val index = fileName.lastIndexOf(".")
        return if (index != -1) {
            fileName.substring(index + 1).toLowerCase(Locale.US)
        } else {
            null
        }
    }

    /**
     * 获取文件类型
     *
     * @param path 文件路径
     * @return
     */
    fun getMimeType(path: String?): String? {
        return getMimeType(File(path))
    }

    /**
     * 获取文件类型
     *
     * @param file 文件
     * @return
     */
    fun getMimeType(file: File?): String? {
        val suffix = getSuffix(file) ?: return "file/*"
        val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)
        return if (type!=null&& type.isNotEmpty())  type  else "file/*"
    }

    /**
     * 创建Url文件名称
     *
     * @param url 资源地址
     * @return
     */
    fun buildNameByUrl(url: String): String {
        if (url.contains("/") && url.contains(".")) {
            return url.substring(url.lastIndexOf("/") + 1)
        }
        val format = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        return format.format(format) + ".zip"
    }

    /**
     * 获取Assets文件内容
     *
     * @param context 上下文
     * @param fileName 文件名
     * @return
     */
    fun readAssets(context: Context, fileName: String?): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf =
                BufferedReader(InputStreamReader(assetManager.open(fileName!!)))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     */
    fun read(file: File?): String {
        if (file == null) {
            return "The read file is empty and cannot be read."
        }
        //定义一个字符串缓存，将字符串存放缓存中
        val sb = StringBuilder("")
        //定义一个fileReader对象，用来初始化BufferedReader
        val reader: FileReader
        try {
            reader = FileReader(file)
            //new一个BufferedReader对象，将文件内容读取到缓存
            val bufferedReader = BufferedReader(reader)
            var content: String
            //逐行读取文件内容，不读取换行符和末尾的空格
            while (bufferedReader.readLine().also { content = it } != null) {
                //将读取的字符串添加换行符后累加存放在缓存中
                sb.append(
                    """
    $content
    
    """.trimIndent()
                )
            }
            bufferedReader.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sb.toString()
    }



    /**
     * 写入文件
     *
     * @param context 上下文
     * @param dir     文件夹名字
     * @param name    文件名字
     * @param content 文件内容
     */
    fun write(
        context: Context,
        dir: String,
        name: String?,
        content: String?
    ) {
        val fileDir =
            File(getExternalCacheDir(context) + File.separator + dir + File.separator)
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        val classFile = File(fileDir.absolutePath, name)
        if (!classFile.exists()) {
            try {
                classFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            val pw = PrintWriter(FileOutputStream(classFile))
            pw.print(content)
            pw.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * 读取Assets文件内容
     *
     * @param context 上下文
     * @param name    文件名
     * @return
     */
    fun openAssets(context: Context, name: String?): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf =
                BufferedReader(InputStreamReader(assetManager.open(name!!)))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    /**
     * 读取文件数据
     *
     * @return
     */
    fun read(path: String?): StringBuffer {
        val stringBuilder = StringBuffer()
        try {
            val bf =
                BufferedReader(InputStreamReader(FileInputStream(path)))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder
    }

    /**
     * 将文件流转成文件
     *
     * @param inputStream 输入流
     * @param path        文件路径
     * @return
     */
    fun decodeInputStream(inputStream: InputStream, path: String?): File {
        val file = File(path) //文件夹
        if (file.parentFile.isDirectory && !file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //写入文件操作流程中
        var len: Int
        val buffer = ByteArray(2048)
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = FileOutputStream(file)
            while (inputStream.read(buffer).also { len = it } != -1) {
                fileOutputStream.write(buffer, 0, len)
            }
            fileOutputStream.flush()
            fileOutputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    /**
     * File转Bytes
     *
     * @param file
     * @return
     */
    fun decodeFile(file: File?): ByteArray? {
        var buffer: ByteArray? = null
        try {
            val fis = FileInputStream(file)
            val bos = ByteArrayOutputStream(1000)
            val b = ByteArray(1000)
            var n: Int
            while (fis.read(b).also { n = it } != -1) {
                bos.write(b, 0, n)
            }
            fis.close()
            bos.close()
            buffer = bos.toByteArray()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return buffer
    }

    /**
     * Bytes转文件
     *
     * @param bytes 字节数据
     * @param path  文件路径
     * @return
     */
    fun decodeBytes(bytes: ByteArray?, path: String?): File {
        var file = File(path)
        if (file.parentFile.exists()) {
            file.mkdirs()
        }
        var bos: BufferedOutputStream? = null
        var fos: FileOutputStream? = null
        try {
            file = File(path)
            fos = FileOutputStream(file)
            bos = BufferedOutputStream(fos)
            bos.write(bytes)
            bos.close()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName 文件名
     * @param cls 资源类型
     * @return 资源id
     */
    fun findResId(variableName: String?, cls: Class<*>): Int {
        return try {
            val idField = cls.getDeclaredField(variableName)
            idField.getInt(idField)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
}