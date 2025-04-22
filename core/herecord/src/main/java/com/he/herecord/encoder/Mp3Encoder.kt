package com.he.herecord.encoder

import android.media.AudioFormat
import android.util.Log
import com.he.herecord.config.RecorderConfig
import com.he.lame.HeLame
import java.io.File

/**
 * @Description
 * @Author HeQuanLi
 * @CreateTime 2025年04月22日 10:05:43
 */
class Mp3Encoder(private val config: RecorderConfig) : Encoder {

    private val outputStream = config.saveFilePath?.let { File(it).outputStream() }

    override fun start() {
        HeLame.init(
            config.sampleRate,
            config.channelConfig,
            config.sampleRate,
            config.bitRate,
            config.quality,
            config.lowPassFreq,
            config.highPassFreq,
            true
        )
    }

    override fun encode(pcmData: ShortArray) {
        //mp3buf_size in bytes = 1.25*num_samples + 7200
        val mp3Data = ByteArray(pcmData.size * 2)
        val encoded = if (config.channelConfig == AudioFormat.CHANNEL_IN_MONO) {
            //单声道
            Log.d("TAG_HQL", "encode: 单声道")
            HeLame.encode(pcmData, pcmData, pcmData.size, mp3Data)
        } else {
            //双声道
            Log.d("TAG_HQL", "encode: 双声道")
            HeLame.encodeInterleaved(pcmData, pcmData.size / 2, mp3Data)
        }
        if (encoded > 0) {
            outputStream?.write(mp3Data, 0, encoded)
        }
    }

    override fun close() {
        try {
            val mp3Data = ByteArray(7200) // 确保缓冲区足够大
            val flushSize = HeLame.flush(mp3Data)
            if (flushSize > 0) {
                outputStream?.write(mp3Data, 0, flushSize)
            }
        } catch (e: Throwable) {
            throw Throwable("flush MP3 文件时发生错误", e)
        } finally {
            try {
                HeLame.close()
                outputStream?.close()
            } catch (e: Throwable) {
                throw Throwable("关闭 MP3 文件时发生错误", e)
            }
        }
    }

    override fun release() {
    }

    override fun getOutputFilePath() = config.saveFilePath
}