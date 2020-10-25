package com.xhstormr.web.domain.model.response

import java.io.File

data class TaskImportResponse(
    val videos: List<File>,
    val taskId: String,
)
