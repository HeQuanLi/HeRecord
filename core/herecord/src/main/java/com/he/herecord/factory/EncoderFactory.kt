package com.he.herecord.factory

import com.he.herecord.config.RecorderConfig
import com.he.herecord.bean.Formats
import com.he.herecord.encoder.Encoder
import com.he.herecord.encoder.Mp3Encoder
import com.he.herecord.encoder.PcmEncoder
import com.he.herecord.encoder.WavEncoder

/**
 * @Description
 * @Author HeQuanLi
 * @CreateTime 2025年04月22日 10:00:10
 */
object EncoderFactory {
    fun createEncoder(config: RecorderConfig): Encoder {
        return when (config.format) {
            Formats.PCM -> PcmEncoder(config)
            Formats.WAV -> WavEncoder(config)
            Formats.MP3 -> Mp3Encoder(config)
        }
    }
}