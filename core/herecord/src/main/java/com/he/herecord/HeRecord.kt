package com.he.herecord

import android.Manifest
import androidx.annotation.RequiresPermission
import com.blankj.utilcode.util.FileUtils
import com.he.herecord.config.RecorderConfig
import com.he.herecord.core.AudioRecorder
import com.he.herecord.factory.EncoderFactory
import com.he.herecord.listener.RecorderListener

/**
 * @Description
 * @Author HeQuanLi
 * @CreateTime 2025年04月22日 10:14:07
 */
object HeRecord {

    private var audioRecorder: AudioRecorder? = null

    fun init(config: RecorderConfig, recorderListener: RecorderListener? = null) {
        checkAudioFilePath(config)
        val encoder = EncoderFactory.createEncoder(config)
        audioRecorder = AudioRecorder(config, encoder, recorderListener)
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startRecord() {
        audioRecorder?.startRecording()
    }

    fun stopRecord() {
        audioRecorder?.stopRecording()
    }

    fun release() {
        audioRecorder?.release()
    }

    //检查录音文件存储路径
    private fun checkAudioFilePath(config: RecorderConfig): Boolean {
        if (!FileUtils.isFileExists(config.saveFilePath)) { //如果文件不存在，则创建
            val result = FileUtils.createOrExistsFile(config.saveFilePath)
            if (!result) {
                return false
            }
        }
        return true
    }
}