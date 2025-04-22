package com.he.demo.record

import android.Manifest
import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.he.herecord.HeRecord
import com.he.herecord.bean.Formats
import com.he.herecord.config.RecorderConfig
import com.he.lame.HeLame
import java.util.Random

/**
 * @Description
 * @Author HeQuanLi
 * @CreateTime 2025年04月19日 17:25:33
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        val tvVersion = findViewById<TextView>(R.id.tvVersion)
        tvVersion.text = HeLame.getVersion()
        findViewById<Button>(R.id.tvStartRecord).setOnClickListener {
            randomRecordCycle()
        }
        findViewById<Button>(R.id.tvEndRecord).setOnClickListener {
            stopRecord()
        }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun startRecord() {
        val config = RecorderConfig.Builder()
            .apply {
                setEncodeFormat(Formats.MP3)
                setChannelConfig(AudioFormat.CHANNEL_IN_MONO)
                setSampleRate(getBestSampleRate())
            }.build()

        HeRecord.init(config)
        HeRecord.startRecord()
    }

    private fun stopRecord() {
        HeRecord.stopRecord()
    }

    //获取最佳采样率
    private fun getBestSampleRate(): Int {
        val am = this.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        val sampleRateStr: String? = am?.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
        val sampleRate: Int = sampleRateStr?.let { str ->
            Integer.parseInt(str).takeUnless { it == 0 }
        } ?: 44100 // Use a default value if property not found
        return sampleRate
    }

    private fun randomRecordCycle() {
        // 创建 Handler 来管理定时任务
        val handler = Handler(Looper.getMainLooper())

        // 创建随机数生成器，用于生成 100ms 到 5000ms 的延迟
        val random = Random()

        // 递归函数，用于调度录音循环
        fun scheduleNext() {
            // 随机生成 100ms 到 5000ms 的开始延迟
            val startDelay = random.nextInt(4901) + 100 // 100 到 5000
            // 随机生成 100ms 到 5000ms 的录音持续时间
            val recordDuration = random.nextInt(4901) + 100 // 100 到 5000

            // 调度 startRecord
            handler.postDelayed({
                try {
                    startRecord()
                    // 调度 stopRecord
                    handler.postDelayed({
                        stopRecord()
                        // 调度下一个循环
                        scheduleNext()
                    }, recordDuration.toLong())
                } catch (e: Exception) {
                    // 处理可能的错误（如权限问题）
                    e.printStackTrace()
                }
            }, startDelay.toLong())
        }

        // 启动第一个循环
        scheduleNext()
    }
}