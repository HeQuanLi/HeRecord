package com.he.herecord.encoder

import com.he.herecord.config.RecorderConfig

/**
 * @Description
 * @Author HeQuanLi
 * @CreateTime 2025年04月22日 09:55:31
 */
class WavEncoder(private val config: RecorderConfig) : Encoder {
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