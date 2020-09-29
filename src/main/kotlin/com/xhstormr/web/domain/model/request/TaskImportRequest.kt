package com.xhstormr.web.domain.model.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.io.File

/**
 * @author zhangzf
 * @create 2018/8/1 9:45
 */
@ApiModel("任务导入请求")
data class TaskImportRequest(
    @field:ApiModelProperty("文件")
    val file: File
)
