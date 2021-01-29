package com.smallcake.temp.http

/**
 * 后端返回数据基类
 * @param <T>
</T> */
class BaseResponse<T> {
    /** 返回码  */
    var reason :String?= null
    var resultcode :Int?= null
    var error_code :Int?= 0

    /** 数据  */
    var result: T? = null
        private set

    fun setResult(result: T) {
        this.result = result
    }

}