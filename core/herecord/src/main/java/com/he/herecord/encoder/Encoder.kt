package com.he.herecord.encoder

/**
 * @Description
 * @Author HeQuanLi
 * @CreateTime 2025年04月22日 09:35:56
 */
interface Encoder {
    /**
     * Start the encoder (e.g., initialize resources, write file header)
     */
    fun start()

    /**
     * Encode a chunk of PCM data
     */
    fun encode(pcmData: ShortArray)

    /**
     * Signal end of stream and finalize encoding (e.g., write final header, close file)
     */
    fun close()

    /**
     * Release resources
     */
    fun release()

    // Get the output file path
    fun getOutputFilePath(): String?
}