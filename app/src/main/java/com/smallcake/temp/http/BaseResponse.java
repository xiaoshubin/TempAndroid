package com.smallcake.temp.http;

public class BaseResponse<T> {
    /** 返回码 */
    private int code;
    /** 提示语 */
    private String message;
    /** 数据 */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
