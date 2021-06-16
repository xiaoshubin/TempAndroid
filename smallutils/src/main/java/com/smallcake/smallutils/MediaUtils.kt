package com.smallcake.smallutils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.SurfaceHolder
import android.widget.MediaController
import android.widget.VideoView


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
     * 使用MediaPlayer播放raw文件下的 mp4到SurfaceView
     * @param context Context
     * @param holder SurfaceHolder 外部页面SurfaceView提供
     * 开始播放 mPlayer.start()
     * 暂停播放 mPlayer.pause()
     * 结束播放 mPlayer.stop()
     */
    fun playMp4(context: Context, holder: SurfaceHolder){
        val mPlayer = MediaPlayer.create(context, R.raw.lesson)
        val audioAttr = AudioAttributes.Builder()
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .setLegacyStreamType(AudioManager.STREAM_ALARM)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
            .build()
        mPlayer.setAudioAttributes(audioAttr)
        mPlayer.setDisplay(holder)
        mPlayer.start()
    }

    /**
     * 使用VideoView播放在线视频
     * @param context Context
     * @param videoView VideoView
     */
    fun playVideo(context: Context,videoView: VideoView){
        val videoUrl = "http://poss.videocloud.cns.com.cn/oss/2020/07/19/chinanews/MEIZI_YUNSHI/onair/F1B171FB2ECB4319ADAC3FF2915C7E4B.mp4"
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        val video: Uri = Uri.parse(videoUrl)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(video)
        videoView.start()
    }
}
