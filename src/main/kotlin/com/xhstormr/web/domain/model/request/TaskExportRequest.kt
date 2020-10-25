package com.xhstormr.web.domain.model.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.io.File

/**
 * @author zhangzf
 * @create 2018/8/1 9:45
 */
@ApiModel("任务导出请求")
data class TaskExportRequest(
    @field:ApiModelProperty("索引")
    val indices: List<String>,
    @field:ApiModelProperty("视频")
    val videos: List<File>,
    @field:ApiModelProperty("任务 ID")
    val taskId: String
)
