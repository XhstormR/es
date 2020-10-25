package com.xhstormr.web.domain.model

/**
 * @author zhangzf
 * @create 2018/8/1 9:45
 */
data class SnapshotManifest(
    val indices: List<String>,
    val videos: List<String>,
    val taskId: String,
)
