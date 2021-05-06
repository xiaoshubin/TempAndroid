package com.smallcake.smallutils

import android.content.Context
import android.media.MediaPlayer


object MediaUtils{
    /**
     * 播放资源下的音频文件
     * 例如：MediaUtils.playMp3("zltx.mp3",R.raw::class.java)
     */
    fun playMp3(name: String, cls: Class<*>) {
        val resId: Int = ResourceUtils.findResId(name.replace(".mp3", ""), cls)
        MediaPlayer.create(SmallUtils.context, resId).start()
    }
}
