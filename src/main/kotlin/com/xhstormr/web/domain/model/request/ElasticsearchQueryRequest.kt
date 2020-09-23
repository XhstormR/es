package com.xhstormr.web.domain.model.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author zhangzf
 * @create 2018/8/1 9:45
 */
@ApiModel(
    "Elasticsearch 检索请求",
    description = "https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-query-string-query.html#query-string-syntax"
)
data class ElasticsearchQueryRequest(
    @field:ApiModelProperty(
        "检索字符串",
        example = "task:1 AND type:log AND label:openvpn AND timestamp:[2020-01-01 TO 2020-12-31]"
    )
    val query: String
)
