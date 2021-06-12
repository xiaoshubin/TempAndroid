package com.smallcake.smallutils

import android.webkit.WebView

/**
 * Date:2021/6/12 17:25
 * Author:SmallCake
 * Desc: WebView工具类
 **/
class WebUtils {
    /**
     * 图片自适应屏幕宽度
     * @return String
     * 更多参考：https://www.jianshu.com/p/d2acd79c3d32
     */
    fun autoImg(content: String?):String?{
        return content?.apply {
            replace("<img", "<img style='max-width:100%;height:auto")
        }
    }

    /**
     * 加载文本内容
     * 图片自适应
     */
    fun WebView.loadContentAutoImg(content: String?){
        this.loadDataWithBaseURL(null,autoImg(content)?: "", "text/html", "utf-8",null)
    }
}