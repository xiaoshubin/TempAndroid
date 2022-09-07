package com.smallcake.temp.http

import android.util.Log
import com.smallcake.temp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import org.json.JSONObject
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

abstract class ResponseBodyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val startNs = System.nanoTime()//请求发起的时间
        val request = chain.request()
        val url = request.url.toString()
        val response = chain.proceed(request)
        if (BuildConfig.DEBUG){
            val params = if (request.method == "POST")"\n====================================参数开始==============================================\n${logParams(request)}\n====================================参数结束==============================================" else ""
            Log.d("SmallOkHttp>>>","【${request.method}】发送请求 ${request.url}$params")
        }

        response.body?.let { responseBody ->
            val contentLength = responseBody.contentLength()
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            var buffer = source.buffer

            if ("gzip".equals(response.headers["Content-Encoding"], ignoreCase = true)) {
                GzipSource(buffer.clone()).use { gzippedResponseBody ->
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                }
            }

            val contentType = responseBody.contentType()
            val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

            if (BuildConfig.DEBUG){
                val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
                val responseBody = response.peekBody(1024*1024)
                val contentLength = responseBody.contentLength()
                val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
                val jsonBody = formatJson(responseBody.string())
                Log.d("SmallOkHttp>>>",
                    "【${request.method}】${response.code} ${response.message} 接收响应 ${response.request.url}(${tookMs}ms)\n${response.headers}\n${jsonBody}\n\nEND HTTP ($bodySize body)")
            }

            if (contentLength != 0L) {
                return intercept(response,url, buffer.clone().readString(charset))
            }
        }

        return response
    }

    abstract fun intercept(response: Response, url: String, body: String): Response
}
private fun logParams(request: Request): String {
    val method = request.method
    if ("POST" == method) {
        val requestBody = request.body
        val buffer = Buffer()
        requestBody!!.writeTo(buffer)
        return buffer.readString(Charset.forName("UTF-8"))
    }
    return "no params"
}
/**
 * 将字符串格式化成 JSON 格式
 */
private fun formatJson(json: String?): String {
    if (json == null) {
        return ""
    }
    // 计数tab的个数
    var tabNum = 0
    val builder = StringBuilder()
    val length = json.length
    var last = 0.toChar()
    for (i in 0 until length) {
        val c = json[i]
        if (c == '{') {
            tabNum++
            builder.append(c).append("\n")
                .append(getSpaceOrTab(tabNum))
        } else if (c == '}') {
            tabNum--
            builder.append("\n")
                .append(getSpaceOrTab(tabNum))
                .append(c)
        } else if (c == ',') {
            builder.append(c).append("\n")
                .append(getSpaceOrTab(tabNum))
        } else if (c == ':') {
            if (i > 0 && json[i - 1] == '"') {
                builder.append(c).append(" ")
            } else {
                builder.append(c)
            }
        } else if (c == '[') {
            tabNum++
            val next = json[i + 1]
            if (next == ']') {
                builder.append(c)
            } else {
                builder.append(c).append("\n")
                    .append(getSpaceOrTab(tabNum))
            }
        } else if (c == ']') {
            tabNum--
            if (last == '[') {
                builder.append(c)
            } else {
                builder.append("\n").append(getSpaceOrTab(tabNum)).append(c)
            }
        } else {
            builder.append(c)
        }
        last = c
    }
    return builder.toString()
}

/**
 * 创建对应数量的制表符
 */
private fun getSpaceOrTab(tabNum: Int): String? {
    val sb = StringBuffer()
    for (i in 0 until tabNum) {
        sb.append('\t')
    }
    return sb.toString()
}

class HandleErrorInterceptor : ResponseBodyInterceptor() {
    override fun intercept(response: Response, url: String, body: String): Response {
                var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (jsonObject != null) {
            if (jsonObject.optInt("code", -1) != 0&&jsonObject.optInt("code", -1) != 200 && jsonObject.has("msg")) {
                throw ApiException(jsonObject.getString("msg"))
            }
        }
        return response
    }

}

class ApiException(msg:String): RuntimeException(msg)


