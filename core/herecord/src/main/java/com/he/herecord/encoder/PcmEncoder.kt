package com.he.herecord.encoder

import com.he.herecord.config.RecorderConfig
import com.he.herecord.encoder.Encoder

/**
 * @Description
 * @Author HeQuanLi
 * @CreateTime 2025年04月22日 09:50:55
 */
class PcmEncoder(private val config: RecorderConfig) : Encoder {
    override fun start() {
    }

    override fun encode(pcmData: ShortArray) {
    }

    override fun close() {
    }

    override fun release() {
    }

    override fun getOutputFilePath() = config.saveFilePath
}