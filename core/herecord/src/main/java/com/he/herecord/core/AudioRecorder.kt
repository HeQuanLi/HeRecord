package com.he.herecord.core

import android.os.Process
import android.Manifest
import android.media.*
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.he.herecord.config.RecorderConfig
import com.he.herecord.encoder.Encoder
import com.he.herecord.listener.RecorderListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

class AudioRecorder(
    private val config: RecorderConfig,
    private val encoder: Encoder,
    private val recorderListener: RecorderListener? = null
) {

    private val audioQueue = LinkedBlockingQueue<ShortArray>() // Thread-safe queue
    private val isRecording = AtomicBoolean(false) // Recording state
    private val executor: ExecutorService = Executors.newFixedThreadPool(2) // Thread pool
    private var audioRecord: AudioRecord? = null
    private val handler = Handler(Looper.getMainLooper()) // Main thread callback
    private var startTime: Long = 0

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startRecording() {
        if (isRecording.get()) {
            return // Prevent duplicate start
        }
        isRecording.set(true)
        // Initialize recording
        audioRecord = AudioRecord(
            config.audioSource,
            config.sampleRate,
            config.channelConfig,
            config.audioFormat,
            config.bufferSize
        ).apply {
            startRecording()
            recorderListener?.onStarted()
        }

        // Start encoder
        encoder.start()

        // Start recording and encoding threads
        executor.submit { recordAudio() }
        executor.submit { encodeAudio() }
    }

    fun stopRecording() {
        if (!isRecording.get()) {
            return
        }
        isRecording.set(false) // Stop recording
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    private fun recordAudio() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
        val buffer = ShortArray(config.bufferSize)
        startTime = System.currentTimeMillis()
        while (isRecording.get()) {
            val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
            if (read > 0) {
                val data = ShortArray(read)
                System.arraycopy(buffer, 0, data, 0, read)
                audioQueue.offer(data) // Put data into queue
            }
        }
        // Signal end of recording
        audioQueue.offer(ShortArray(0))
        recorderListener?.onStopped()
    }

    private fun encodeAudio() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
        var isEos = false
        while (!isEos) {
            // Get data from queue
            val data = audioQueue.take()
            if (data.isEmpty()) {
                isEos = true // Empty data indicates end
                encoder.close()
            } else {
                encoder.encode(data)
            }
        }
        val duration = System.currentTimeMillis() - startTime
        // Callback with encoded file path
        handler.post {
            recorderListener?.onComplete(config.saveFilePath, duration)
        }
    }

    fun release() {
        stopRecording()
        encoder.release()
        executor.shutdown()
    }
}