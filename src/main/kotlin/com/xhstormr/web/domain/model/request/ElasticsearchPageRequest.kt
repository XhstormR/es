package com.xhstormr.web.domain.model.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.Range

/**
 * @author zhangzf
 * @create 2018/8/1 9:45
 */
@ApiModel("Elasticsearch 分页请求")
data class ElasticsearchPageRequest(
    @field:ApiModelProperty("页码")
    val afterKey: Map<String, Any>?,
    @field:Range(message = "非法分页大小", min = 1, max = 100)
    @field:ApiModelProperty("分页大小", example = 5.toString())
    val size: Int = 5
)
