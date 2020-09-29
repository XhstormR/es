package com.xhstormr.web.app.controller

import com.xhstormr.web.domain.model.request.ElasticsearchQueryRequest
import com.xhstormr.web.domain.model.request.PageRequest
import com.xhstormr.web.domain.service.ElasticsearchSharedService
import com.xhstormr.web.domain.service.TaskService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * @author zhangzf
 * @create 2020/7/16 13:12
 */
@Api(tags = ["任务分析接口"])
@RestController
@RequestMapping("/task/{taskIndex:^topic-.+$}", produces = [MediaType.APPLICATION_JSON_VALUE])
class TaskAnalysisController(
    private val taskService: TaskService,
    private val elasticsearchSharedService: ElasticsearchSharedService
) : BaseController() {

    @ApiOperation("任务检索")
    @PostMapping("/query")
    fun queryTask(
        @ApiParam("分页请求") @Valid pageRequest: PageRequest,
        @ApiParam("检索请求") @Valid @RequestBody request: ElasticsearchQueryRequest,
        @ApiParam("任务索引") @PathVariable taskIndex: String
    ) =
        elasticsearchSharedService.stringQuery(request.query, taskIndex, pageRequest)

    @ApiOperation("任务字段")
    @GetMapping("/field")
    fun getTaskField(@ApiParam("任务索引") @PathVariable taskIndex: String) =
        elasticsearchSharedService.getMappings(taskIndex)

    @ApiOperation("任务导出")
    @PostMapping("/export")
    fun exportTask(@ApiParam("任务索引") @PathVariable taskIndex: String) =
        taskService.exportTask(taskIndex)
}
