package com.xhstormr.web.app.controller

import com.xhstormr.web.app.config.Const
import com.xhstormr.web.domain.model.request.ElasticsearchQueryRequest
import com.xhstormr.web.domain.model.request.FileImportRequest
import com.xhstormr.web.domain.model.request.PageRequest
import com.xhstormr.web.domain.service.ElasticsearchSharedService
import com.xhstormr.web.domain.service.TaskService
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
@Api(tags = ["任务接口"])
@RestController
@RequestMapping("/task", produces = [MediaType.APPLICATION_JSON_VALUE])
class TaskController(
    private val taskService: TaskService,
    private val elasticsearchSharedService: ElasticsearchSharedService
) : BaseController() {

    @ApiOperation("任务检索")
    @PostMapping("/query")
    fun queryTask(
        @ApiParam("分页请求") @Valid pageRequest: PageRequest,
        @ApiParam("检索请求") @Valid @RequestBody request: ElasticsearchQueryRequest
    ) =
        elasticsearchSharedService.stringQuery(request.query, Const.INDICES, pageRequest)

    @ApiOperation("任务列表")
    @GetMapping
    fun getTaskList() =
        elasticsearchSharedService.getIndex(Const.INDICES)

    @ApiOperation("任务字段")
    @GetMapping("/field")
    fun getTaskField() =
        elasticsearchSharedService.getMappings(Const.INDICES)

    @ApiOperation("任务导入")
    @PostMapping("/import")
    fun importTask(
        @ApiParam("任务导入请求") @Valid @RequestBody request: FileImportRequest
    ) =
        taskService.importTask(request.file)
}
