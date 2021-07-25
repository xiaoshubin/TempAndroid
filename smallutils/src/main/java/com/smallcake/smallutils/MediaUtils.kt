package com.smallcake.smallutils

import android.app.Activity
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import android.view.SurfaceHolder
import android.widget.MediaController
import android.widget.VideoView
import java.io.File
import java.io.IOException
import java.util.*


object MediaUtils{
    private const val TAG = "MediaUtils"
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
    var mMediaRecorder:MediaRecorder?=null
    var filePath = ""
    fun startRecord(activity: Activity) {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null) mMediaRecorder = MediaRecorder()
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC) // 设置麦克风
            /*
         * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
         * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
         */mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */mMediaRecorder?.setAudioEncoder(
                MediaRecorder.AudioEncoder.AAC
            )
            val fileName = "${System.currentTimeMillis()}.m4a"

             filePath = "${activity.externalCacheDir}/$fileName"
            /* ③准备 */mMediaRecorder?.setOutputFile(filePath)
            mMediaRecorder?.prepare()
            /* ④开始 */mMediaRecorder?.start()
        } catch (e: IllegalStateException) {
            Log.i(">>>","call startAmr(File mRecAudioFile) failed!" + e.message)
        } catch (e: IOException) {
            Log.i(">>>","call startAmr(File mRecAudioFile) failed!" + e.message)
        }
    }

    /**
     * 停止录音
     */
    fun stopRecord() {
        try {
            mMediaRecorder!!.stop()
            mMediaRecorder!!.release()
            mMediaRecorder = null

        } catch (e: RuntimeException) {
            Log.i(">>>",e.toString())
            mMediaRecorder!!.reset()
            mMediaRecorder!!.release()
            mMediaRecorder = null
            val file = File(filePath)
            if (file.exists()) file.delete()
            filePath = ""
        }
    }
    /**
     * 播放音频
     * @param audioPath String
     */
    fun playVoice(audioPath: String){
        try {
            val mediaPlayer =  MediaPlayer()
            mediaPlayer.setDataSource(audioPath)
            val attributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build()
            mediaPlayer.setAudioAttributes(attributes)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener{
                mediaPlayer.start()
            }

        } catch (e: Exception) {
        }
    }

    /**
     * 获取音频时长
     * @param filePath String?
     * @return Int 秒
     */
    fun getAudioFileVoiceTime(filePath: String?): Int {
        var mediaPlayerDuration = 0L
        if (filePath.isNullOrEmpty()) return 0
        val mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(filePath)
            mediaPlayer.prepare()
            mediaPlayerDuration = mediaPlayer.duration.toLong()
        } catch (ioException: IOException) {
            ioException.message?.let { Log.e(TAG, it) }
        }
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
        return (mediaPlayerDuration/1000).toInt()
    }
}
