package com.he.hens

/**
 * @Description 自动增益控制
 * @Author HeQuanLi
 * @CreateTime 2024年12月10日 16:18:05
 */
object AutomaticGainControl {

    init {
        System.loadLibrary("legacy_agc-lib")
    }

    /**
     * @return 自适应增益控制 id
     */
    external fun agcCreate(): Long

    /**
     * @param agcInst 自适应增益控制实例 id
     * @param minLevel 麦克风最小音量值
     * @param maxLevel 麦克风最大音量值
     * @param agcMode 增益控制模式。
     *                  0 - 不变
     *                  1 - 自适应模拟自动增益控制 -3dBOv
     *                  2 - 自适应数字自动增益控制 -3dBOv
     *                  3 - 固定数字增益 0dB
     * @param fs 采样率
     * @param targetLevelDbfs 目标电平以包络线的 -dBfs 为单位（默认为 -3）
     * @param compressionGaindB 固定增益水平（dB）
     * @param limiterEnable 启用限制器（开/关（默认关闭））
     *
     */
    external fun agcInit(
        agcInst: Long,
        minLevel: Int,
        maxLevel: Int,
        agcMode: Int,
        fs: Int,
        targetLevelDbfs: Short,
        compressionGaindB: Short,
        limiterEnable: Boolean
    ): Int

    /**
     * @param agcInst 自适应增益控制实例 id
     * @param inNear 输入数据
     * @param numBands 输入/输出向量中的频段数
     * @param samples 采样率
     * @param out 输出数据
     * @param inMicLevel 当前麦克风音量
     * @param outMicLevel 调整后的麦克风音量
     * @param echo 如果传递给 add_mic 的信号几乎肯定没有回声，则设置为 0；否则设置为 1。如果您没有关于回声的信息，则设置为 0。
     * @param saturationWarning 返回值 1 表示已发生饱和事件并且音量无法进一步降低。否则将设置为 0。
     * @return 0成功，否则。失败。
     */
    external fun agcProcess(
        agcInst: Long,
        inNear: ShortArray?,
        numBands: Int,
        samples: Int,
        out: ShortArray?,
        inMicLevel: Int,
        outMicLevel: Int,
        echo: Int,
        saturationWarning: Boolean
    ): Int

    /**
     * @param agcInst 自适应增益控制实例 id
     */
    external fun agcFree(agcInst: Long): Int
}