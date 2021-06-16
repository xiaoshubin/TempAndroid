package com.smallcake.smallutils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SurfaceHolder


object MediaUtils{
    /**
     * 播放res/raw资源下的mp3音频文件
     * 例如：MediaUtils.playMp3("zltx.mp3",R.raw::class.java)
     */
    fun playMp3(name: String, cls: Class<*>) {
        val resId: Int = ResourceUtils.findResId(name.replace(".mp3", ""), cls)
        MediaPlayer.create(SmallUtils.context, resId).start()
    }

    /**
     * 播放raw文件下的 mp4
     * @param name String 本地视频名称
     * @param cls Class<*>
     * @param holder SurfaceHolder
     */
    fun playMp4(context: Context, holder: SurfaceHolder,name: String){
        val resId: Int = ResourceUtils.findResId(name.replace(".mp4", ""), R.raw::class.java)
        val mPlayer = MediaPlayer.create(context, resId)
        val audioAttr = AudioAttributes.Builder()
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .setLegacyStreamType(AudioManager.STREAM_ALARM)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
            .build()
        mPlayer.setAudioAttributes(audioAttr)
        mPlayer.setDisplay(holder)
    }
}
