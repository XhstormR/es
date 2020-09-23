package com.xhstormr.web.domain.model.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Min

/**
 * @author zhangzf
 * @create 2018/8/1 9:45
 */
@ApiModel("分页请求")
data class PageRequest(
    @field:Min(message = "非法页码", value = 0)
    @field:ApiModelProperty("页码", example = 0.toString())
    val page: Int = 0,
    @field:Range(message = "非法分页大小", min = 1, max = 100)
    @field:ApiModelProperty("分页大小", example = 5.toString())
    val size: Int = 5
) {
    @ApiModelProperty(hidden = true)
    val offset = page * size
}
