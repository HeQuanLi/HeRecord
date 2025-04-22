package com.he.hens

/**
 * @Description 降噪
 * @Author HeQuanLi
 * @CreateTime 2024年12月10日 16:52:37
 */
object NoiseSuppressor {

    init {
        System.loadLibrary("legacy_ns-lib")
    }

    /**
     * @return 降噪实例句柄id
     */
    external fun nsxCreate(): Long

    /**
     * @param nsxHandler 降噪实例句柄id
     * @param frequency 采样率
     * @param mode 噪声抑制方法的强度。0：轻微，1：中等，2：强烈
     * @return 0：成功，否则失败
     *
     */
    external fun nsxInit(nsxHandler: Long, frequency: Int, mode: Int): Int

    /**
     * @param nsxHandler 降噪实例句柄id
     * @param speechFrame 输入数据
     * @param numBands 输入/输出向量中的频段数
     * @param outFrame 输出数据
     * @return 0：成功，否则失败
     */
    external fun nsxProcess(
        nsxHandler: Long,
        speechFrame: ShortArray,
        numBands: Int,
        outFrame: ShortArray
    ): Int

    /**
     * @param nsxHandler 降噪实例句柄id
     */
    external fun nsxFree(nsxHandler: Long): Int
}