package com.he.herecord.config

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.he.herecord.bean.Formats
import com.he.herecord.utils.RecordPathUtils

/**
 * @Description
 * @Author HeQuanLi
 * @CreateTime 2025年04月21日 17:16:35
 */
class RecorderConfig private constructor(
    val audioSource: Int,               // 音频源，默认麦克风
    val sampleRate: Int,                // 采样率
    val channelConfig: Int,             // 声道数，默认AudioFormat.CHANNEL_IN_MONO,单声道
    val audioFormat: Int,               // 音频数据格式，默认：PCM 16 位/样本
    val bitRate: Int,                   // 比特率：默认128k.
    val quality: Int,                   // 录音质量，0-9、0：最佳（慢）9：最差（快）推荐：3、5、7，默认5
    val lowPassFreq: Int,               // –lowPass freq	设定低通滤波器的起始点为 freq 高于这个频率的声音会被截除。 Hz -1=no filter
    val highPassFreq: Int,              // –highpass freq	设定高通滤波起始点为 freq 低于这个频率的声音会被截除。 Hz -1=no filter
    val bufferSize: Int,                // 缓冲区大小
    val format: Formats,                // 编码格式。默认原始PCM
    val maxRecordTime: Int,             // 最大录音时长(单位：秒)，默认：-1:无限时长
    val saveFilePath: String? = null,   // 文件保存地址，默认沙盒地址。/data/data/package/cache/Record/
) {
    class Builder {
        private var audioSource: Int = MediaRecorder.AudioSource.MIC
        private var sampleRate: Int = 44100
        private var channelConfig: Int = AudioFormat.CHANNEL_IN_MONO
        private var audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT
        private var bitRate: Int = 128
        private var quality: Int = 5
        private var lowPassFreq: Int = -1
        private var highPassFreq: Int = -1
        private var format: Formats = Formats.PCM
        private var maxRecordTime: Int = -1
        private var saveFilePath: String? = null

        fun setAudioSource(audioSource: Int) = apply { this.audioSource = audioSource }
        fun setSampleRate(sampleRate: Int) = apply { this.sampleRate = sampleRate }
        fun setChannelConfig(channelConfig: Int) = apply { this.channelConfig = channelConfig }
        fun setAudioFormat(audioFormat: Int) = apply { this.audioFormat = audioFormat }
        fun setBitRate(bitRate: Int) = apply { this.bitRate = bitRate }
        fun setQuality(quality: Int) = apply { this.quality = quality }
        fun setLowPassFreq(lowPassFreq: Int) = apply { this.lowPassFreq = lowPassFreq }
        fun setHighPassFreq(highPassFreq: Int) = apply { this.highPassFreq = highPassFreq }
        fun setEncodeFormat(format: Formats) = apply { this.format = format }
        fun setMaxRecordTime(maxRecordTime: Int) = apply { this.maxRecordTime = maxRecordTime }
        fun setSaveFilePath(path: String) = apply { this.saveFilePath = path }

        fun build(): RecorderConfig {
            val bufferSize = calculateBufferSize(sampleRate, channelConfig, audioFormat)
            return RecorderConfig(
                audioSource,
                sampleRate,
                channelConfig,
                audioFormat,
                bitRate,
                quality,
                lowPassFreq,
                highPassFreq,
                bufferSize,
                format,
                maxRecordTime,
                saveFilePath = saveFilePath ?: RecordPathUtils.getDefaultRecordFile(format),
            )
        }

        private fun calculateBufferSize(
            sampleRate: Int,
            channelConfig: Int,
            audioFormat: Int
        ): Int {
            val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            return maxOf(minBufferSize, 4096) // Ensure sufficient buffer
        }
    }

    companion object {
        fun builder(): Builder = Builder()
    }
}