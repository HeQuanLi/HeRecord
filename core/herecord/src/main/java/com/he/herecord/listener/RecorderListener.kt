package com.he.herecord.listener

/**
 * @Description
 * @Author HeQuanLi
 * @CreateTime 2025年04月21日 17:40:27
 */
interface RecorderListener {
    fun onStarted()
    fun onStopped()
    fun onComplete(path: String?, duration: Long)
    fun onError(error: String)
}