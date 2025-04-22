package com.he.herecord.utils

import com.blankj.utilcode.util.PathUtils
import com.he.herecord.bean.Formats

/**
 * @Description
 * @Author HeQuanLi
 * @CreateTime 2024年12月05日 13:16:40
 */
object RecordPathUtils {

    /**
     * 录音默认存储路径。/data/data/package/cache/Record/
     */
    private fun getDefaultRecordPath(): String {
        return PathUtils.getInternalAppCachePath() + "/Record/"
    }

    /**
     * 本地录音文件地址,格式mp3
     */
    fun getDefaultRecordFile(formats: Formats): String {
        return "${getDefaultRecordPath()}${System.currentTimeMillis()}.${formats.name.lowercase()}"
    }
}