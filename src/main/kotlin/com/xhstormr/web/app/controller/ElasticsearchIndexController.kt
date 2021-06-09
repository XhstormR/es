package com.xhstormr.web.app.controller

import com.xhstormr.web.domain.model.request.IndexImportRequest
import com.xhstormr.web.domain.service.ElasticsearchSharedService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * @author zhangzf
 * @create 2020/7/16 13:12
 */
@Api(tags = ["Elasticsearch 索引接口"])
@RestController
@RequestMapping("/es/index/{index}", produces = [MediaType.APPLICATION_JSON_VALUE])
class ElasticsearchIndexController(
    private val elasticsearchSharedService: ElasticsearchSharedService
) : BaseController() {

    @ApiOperation("索引导入")
    @PostMapping
    fun importIndex(
        @ApiParam("索引导入请求") @Valid @RequestBody request: IndexImportRequest,
        @ApiParam("索引") @PathVariable index: String
    ) =
        elasticsearchSharedService.importIndex(index, request.file)

    @ApiOperation("索引导入(追加)")
    @PutMapping
    fun putIndex(
        @ApiParam("索引导入请求") @Valid @RequestBody request: IndexImportRequest,
        @ApiParam("索引") @PathVariable index: String
    ) =
        elasticsearchSharedService.importIndex(index, request.file, true)

    @ApiOperation("索引删除")
    @DeleteMapping
    fun deleteIndex(
        @ApiParam("索引") @PathVariable index: String
    ) =
        elasticsearchSharedService.deleteIndex(index)
}
