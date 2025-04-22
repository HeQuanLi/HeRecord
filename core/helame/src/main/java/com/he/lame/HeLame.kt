package com.he.lame

object HeLame {

    init {
        System.loadLibrary("Lame")
    }

    external fun getVersion(): String

    /**
     * 初始化 lame
     * @param inSampleRate  采样率：越大越接近原声
     * @param inChannel     声道数量
     * @param outSampleRate 采样率：越大越接近原声
     * @param outBitrate    比特率：影响声音的音质
     * @param quality       0-9：
     * @param lowPassFreq   –lowPass freq	设定低通滤波器的起始点为 freq 高于这个频率的声音会被截除。 Hz -1=no filter
     * @param highPassFreq  –highpass freq	设定高通滤波起始点为 freq 低于这个频率的声音会被截除。 Hz -1=no filter
     * @param enableLog     是否输出lame的日志【错误，警告，DEBUG】
     */
    external fun init(
        inSampleRate: Int,
        inChannel: Int,
        outSampleRate: Int,
        outBitrate: Int,
        quality: Int,
        lowPassFreq: Int,
        highPassFreq: Int,
        enableLog: Boolean //是否输出日志
    )

    /**
     * 如果单声道使用该方法
     * @param samples 每通道样本数 =bufferLeft.size
     *
     * @return      number of bytes output in mp3buf. Can be 0
     *                 -1:  mp3buf was too small
     *                 -2:  malloc() problem
     *                 -3:  lame_init_params() not called
     *                 -4:  psycho acoustic problems
     */
    external fun encode(
        bufferLeft: ShortArray,
        bufferRight: ShortArray,
        samples: Int,
        mp3buf: ByteArray
    ): Int

    /**
     * 双声道使用该方法
     * samples = pcm.size/2
     * @return      number of bytes output in mp3buf. Can be 0
     *                 -1:  mp3buf was too small
     *                 -2:  malloc() problem
     *                 -3:  lame_init_params() not called
     *                 -4:  psycho acoustic problems
     */
    external fun encodeInterleaved(
        pcm: ShortArray,
        samples: Int,
        mp3buf: ByteArray
    ): Int

    external fun flush(mp3buf: ByteArray): Int

    external fun close()
}