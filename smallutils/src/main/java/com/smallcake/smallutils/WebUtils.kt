package com.smallcake.smallutils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Date:2021/6/12 17:25
 * Author:SmallCake
 * Desc: WebView工具类
 **/
object WebUtils {
    private val TAG = "WebUtils"
    /**
     * 图片自适应屏幕宽度
     * @return String
     * 更多参考：https://www.jianshu.com/p/d2acd79c3d32
     */
    fun autoImg(content: String?):String?{
        return content?.run {
            replace("<img", "<img style=\"max-width:100%;height:auto\"")
        }
    }

    /**
     * 加载文本内容
     * 图片自适应
     */
    fun loadContentAutoImg(webView: WebView, content: String?){
        webView.loadDataWithBaseURL(null, autoImg(content) ?: "", "text/html", "utf-8", null)
    }

    /**
     * 跳外部浏览器
     * @param url String
     */
    fun goWebExt(url:String){
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        SmallUtils.context?.startActivity(intent)
    }

    /**
     * 注入js 循环遍历网页中的图片控件并和其交互，点击显示大图
     * @param context Context
     * @param webView WebView
     * @param cb Function2<String, Int, Unit>
     */
    fun injectOpenImgJs(context: Context, webView: WebView, cb:(String, Int, List<String>)->Unit){
        webView.webViewClient = MyWebViewClient()
        webView.addJavascriptInterface(JavascriptInterface(context,cb), "imagelistner")
    }
    /**
     * 截取富文本中的图片链接
     * @param content
     * @return
     */
    fun getImgUrlsFromHtml(content: String): Array<String?>? {
        val imageSrcList: MutableList<String?> = ArrayList()
        val p: Pattern = Pattern.compile(
            "<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic|\\b)\\b)[^>]*>",
            Pattern.CASE_INSENSITIVE
        )
        val m: Matcher = p.matcher(content)
        var quote: String? = null
        var src: String? = null
        while (m.find()) {
            quote = m.group(1)
            src =
                if (quote == null || quote.trim { it <= ' ' }.isEmpty()) m.group(2).split("//s+")
                    .get(0) else m.group(2)
            imageSrcList.add(src)
        }
        if (imageSrcList == null || imageSrcList.size == 0) {
            Log.w(TAG, "资讯中未匹配到图片链接")
            return null
        }
        return imageSrcList.toTypedArray()
    }
    /**
     * 避免白屏
     */
    fun whitePageRepair(webView: WebView){
        webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true
            databaseEnabled = true
            setAppCacheEnabled(true)
            allowFileAccess = true
            savePassword = true
            setSupportZoom(true)
            builtInZoomControls = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            useWideViewPort = true
        }
    }

    /**
     * 清除富文本中得图片信息，用于列表数据显示
     * @param content String
     * @return String
     */
    fun clearImgTag(content:String):String{
        var contentNew=content
        if (!TextUtils.isEmpty(content) && content.indexOf("<img") !== -1) {
            val regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>"
            val p_image: Pattern = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE)
            val m_image: Matcher = p_image.matcher(content)
            //循环去掉img标签
            while (m_image.find()) {
                val group = m_image.group()
                contentNew = contentNew.replace(group, "")
            }
        }
        return contentNew
    }
}
class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return super.shouldOverrideUrlLoading(view, url)
    }

    override fun onPageFinished(webView: WebView, url: String) {
        webView.settings.javaScriptEnabled = true
        super.onPageFinished(webView, url)
        addImageClickListner(webView)
    }

    private fun addImageClickListner(webView: WebView) {
        Log.d("WebUtils","addImageClickListner: 注入代码")
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
        webView.loadUrl("javascript:(function (){"
                + "var objs = document.getElementsByTagName(\"img\");"
                + "var imgs = '';"
                + "for(var i=0;i<objs.length;i++){"
                + "objs[i].index = i;"
                + "imgs += objs[i].src+',';"
                + "objs[i].onclick=function(){imagelistner.openImage(this.src,this.index,imgs);}  "
                + "} })()"
        )
    }

    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        view.settings.javaScriptEnabled = true
        Log.d("WebUtils","addImageClickListner: 开始加载")
        super.onPageStarted(view, url, favicon)
    }

    override fun onReceivedError(
        view: WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        super.onReceivedError(view, errorCode, description, failingUrl)
    }
}
// js通信接口
class JavascriptInterface(val context: Context, val cb: (String, Int, List<String>) -> Unit) {
    @android.webkit.JavascriptInterface
    fun openImage(img: String,position:Int,imgs:String) {
        Log.d("WebUtils","要打开的图片$img 点击了第$position 张图片 imgs:$imgs")
        val imgStr = imgs.subSequence(0,imgs.length-1)
        if (imgStr.contains(",")){
            val imgList = imgStr.split(",")
            cb.invoke(img,position,imgList)
        }else{
            val imgList = listOf(img)
            cb.invoke(img,position,imgList)
        }


    }

}