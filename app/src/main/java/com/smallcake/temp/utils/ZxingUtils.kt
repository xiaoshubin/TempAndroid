package com.smallcake.temp.utils

/**
 * Date: 2020/1/14
 * author: SmallCake
 * 需要引入
    implementation 'com.google.zxing:core:3.3.3'
    implementation 'com.journeyapps:zxing-android-embedded:3.6.0'
 * 1.生成二维码：[ZxingUtils.createQRCode]
 * 2.扫描二维码：
 * IntentIntegrator integrator = new IntentIntegrator(GameFragment.this.getActivity());
 * integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
 * integrator.setPrompt("扫一扫");
 * integrator.setCameraId(0);  // Use a specific camera of the device
 * integrator.setBeepEnabled(false);
 * integrator.setBarcodeImageEnabled(true);
 * integrator.initiateScan();

 */
//object ZxingUtils {
//    private var IMAGE_HALFWIDTH = 50 //宽度值，影响中间图片大小
//    private var barcodeEncoder: BarcodeEncoder? = null
//
//    /**
//     * 生成二维码,默认500大小
//     * @param contents 需要生成二维码的文字、网址等
//     * @return bitmap
//     */
//    fun createQRCode(contents: String?): Bitmap? {
//        try {
//            return barcodeEncoder!!.encodeBitmap(contents, BarcodeFormat.QR_CODE, 500, 500)
//        } catch (e: WriterException) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//    /**
//     * 生成二维码
//     * @param contents 需要生成二维码的文字、网址等
//     * @param size 需要生成二维码的大小（）
//     * @return bitmap
//     */
//    fun createQRCode(contents: String?, size: Int): Bitmap? {
//        try {
//            return barcodeEncoder!!.encodeBitmap(
//                contents,
//                BarcodeFormat.QR_CODE,
//                size,
//                size
//            )
//        } catch (e: WriterException) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//    /**
//     * 生成二维码
//     * @param contents 需要生成二维码的文字、网址等
//     * @param size 需要生成二维码的大小（）
//     * @param whiteBorderScale 白边宽度比例，最低1，也就是二维码图片的1%白边
//     * @return bitmap
//     */
//    fun createQRCode(contents: String?, size: Int, whiteBorderScale: Int): Bitmap? {
//        try {
//            val hints =
//                HashMap<EncodeHintType, Any?>()
//            hints[EncodeHintType.MARGIN] = if (whiteBorderScale < 0) 1 else whiteBorderScale
//            return barcodeEncoder!!.encodeBitmap(
//                contents,
//                BarcodeFormat.QR_CODE,
//                size,
//                size,
//                hints
//            )
//        } catch (e: WriterException) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//    /**
//     * 有logo的二维码
//     * @param content
//     * @param size
//     * @param logo
//     * @return
//     */
//    fun createQRCode(content: String, size: Int, logo: Bitmap): Bitmap? {
//        return createQRCodeWithLogo(content, size, logo, 1)
//    }
//
//    fun createQRCode(
//        content: String,
//        size: Int,
//        logo: Bitmap,
//        whiteBorderScale: Int
//    ): Bitmap? {
//        return createQRCodeWithLogo(content, size, logo, whiteBorderScale)
//    }
//
//    /**
//     * 生成带logo的二维码，logo默认为二维码的1/5
//     *
//     * @param contents 需要生成二维码的文字、网址等
//     * @param size 需要生成二维码的大小（）
//     * @param logo logo文件
//     * @return bitmap
//     */
//    private fun createQRCodeWithLogo(
//        contents: String,
//        size: Int,
//        logo: Bitmap,
//        whiteBorderScale: Int
//    ): Bitmap? {
//        var logo = logo
//        return try {
//            IMAGE_HALFWIDTH = size / 10
//            val hints =
//                Hashtable<EncodeHintType, Any?>()
//            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
//            /*
//             * 设置容错级别，默认为ErrorCorrectionLevel.L
//             * 因为中间加入logo所以建议你把容错级别调至H,否则可能会出现识别不了
//             */hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
//            hints[EncodeHintType.MARGIN] = if (whiteBorderScale < 0) 1 else whiteBorderScale
//            val bitMatrix =
//                QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, size, size, hints)
//            val width = bitMatrix.width //矩阵高度
//            val height = bitMatrix.height //矩阵宽度
//            val halfW = width / 2
//            val halfH = height / 2
//            val m = Matrix()
//            val sx = 2.toFloat() * IMAGE_HALFWIDTH / logo.width
//            val sy = 2.toFloat() * IMAGE_HALFWIDTH / logo.height
//            m.setScale(sx, sy)
//            //设置缩放信息
//            //将logo图片按martix设置的信息缩放
//            logo = Bitmap.createBitmap(logo, 0, 0, logo.width, logo.height, m, false)
//            val pixels = IntArray(size * size)
//            for (y in 0 until size) {
//                for (x in 0 until size) {
//                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH && y < halfH + IMAGE_HALFWIDTH
//                    ) {
//                        //该位置用于存放图片信息
//                        //记录图片每个像素信息
//                        pixels[y * width + x] = logo.getPixel(
//                            x - halfW
//                                    + IMAGE_HALFWIDTH,
//                            y - halfH + IMAGE_HALFWIDTH
//                        )
//                    } else {
//                        if (bitMatrix[x, y]) {
//                            pixels[y * size + x] = -0x1000000
//                        } else {
//                            pixels[y * size + x] = -0x1
//                        }
//                    }
//                }
//            }
//            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
//            bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
//            bitmap
//        } catch (e: WriterException) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    init {
//        barcodeEncoder = BarcodeEncoder()
//    }
//}