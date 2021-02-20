package com.smallcake.temp.http

import android.util.Log
import com.smallcake.temp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class HttpLogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val startNs = System.nanoTime()//请求发起的时间
            if (request.method == "POST") {
                if (BuildConfig.DEBUG)Log.d("SmallOkHttp>>>", "【${request.method}】发送请求 ${request.url}====================================参数开始==============================================${logParams(request)}====================================参数结束==============================================")
            } else {
                if (BuildConfig.DEBUG)Log.d("SmallOkHttp>>>","【${request.method}】发送请求 ${request.url}")
            }
            val response: Response
            try {
                response = chain.proceed(request)
            } catch (e: Exception) {
                Log.e("SmallOkHttp>>>","<-- HTTP FAILED: $e")
                throw e
            }
            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            val responseBody = response.peekBody(1024*1024)
            val contentLength = responseBody.contentLength()
            val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
            if (BuildConfig.DEBUG)Log.d("SmallOkHttp>>>",
                    "【${request.method}】${response.code} ${response.message} 接收响应 ${response.request.url}(${tookMs}ms)\n${response.headers}\n${formatJson(responseBody.string())}\n\nEND HTTP ($bodySize body)")

        return response
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
}