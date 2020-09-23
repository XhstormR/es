package com.xhstormr.web.app.controller

import com.xhstormr.web.domain.model.DataTypeGroup
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author zhangzf
 */
@Api(tags = ["字典接口"])
@RestController
@RequestMapping("/dict", produces = [MediaType.APPLICATION_JSON_VALUE])
class DictController : BaseController() {

    @ApiOperation("数据类型组")
    @GetMapping("/dataTypeGroup")
    fun getDataTypeGroup() = DataTypeGroup.values()
}
