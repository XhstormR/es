package com.xhstormr.web.app.controller

import com.xhstormr.web.domain.model.request.ElasticsearchPageRequest
import com.xhstormr.web.domain.model.request.ElasticsearchQueryRequest
import com.xhstormr.web.domain.model.request.PageRequest
import com.xhstormr.web.domain.service.ElasticsearchService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * @author zhangzf
 * @create 2020/7/16 13:12
 */
@Api(tags = ["任务检索接口"])
@RestController
@RequestMapping("/task", produces = [MediaType.APPLICATION_JSON_VALUE])
class ElasticsearchController(
    private val elasticsearchService: ElasticsearchService
) : BaseController() {

    @ApiOperation("任务检索")
    @PostMapping("/query")
    fun queryTask(
        @ApiParam("分页请求") @Valid pageRequest: PageRequest,
        @ApiParam("检索请求") @Valid @RequestBody request: ElasticsearchQueryRequest
    ) = elasticsearchService.queryTask(request, pageRequest)

    @ApiOperation("任务列表")
    @PostMapping
    fun getTaskList(
        @ApiParam("分页请求") @Valid @RequestBody request: ElasticsearchPageRequest
    ) = elasticsearchService.getTaskList(request)

    @ApiOperation("任务总数")
    @GetMapping("/total")
    fun getTaskCount() = elasticsearchService.getTaskCount()

    @ApiOperation("任务字段")
    @GetMapping("/field")
    fun getTaskField() = elasticsearchService.getTaskField()
}
