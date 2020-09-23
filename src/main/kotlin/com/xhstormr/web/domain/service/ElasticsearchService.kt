package com.xhstormr.web.domain.service

import com.xhstormr.web.app.config.Const
import com.xhstormr.web.domain.model.request.ElasticsearchPageRequest
import com.xhstormr.web.domain.model.request.ElasticsearchQueryRequest
import com.xhstormr.web.domain.model.request.PageRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.GetMappingsRequest
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHits
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregation
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder
import org.elasticsearch.search.aggregations.metrics.Cardinality
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.SortOrder
import org.springframework.stereotype.Service

@Service
class ElasticsearchService(
    private val client: RestHighLevelClient
) {

    fun getTaskCount(): Long {
        val name = "task_count"
        val aggregationBuilder = AggregationBuilders
            .cardinality(name)
            .field("task.keyword")
        val searchSourceBuilder = SearchSourceBuilder.searchSource()
            .size(0)
            .aggregation(aggregationBuilder)
        val searchRequest = SearchRequest(Const.INDICES)
            .source(searchSourceBuilder)
        val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
        val aggregation: Cardinality = searchResponse.aggregations[name]
        return aggregation.value
    }

    fun getTaskList(request: ElasticsearchPageRequest): Map<String, Any> {
        val name = "task_id"
        val valuesSourceBuilder = TermsValuesSourceBuilder(name)
            .field("task.keyword")
            .order(SortOrder.DESC)
        val aggregationBuilder = AggregationBuilders
            .composite(name, listOf(valuesSourceBuilder))
            .size(request.size)
            .aggregateAfter(request.afterKey)
        val searchSourceBuilder = SearchSourceBuilder.searchSource()
            .size(0)
            .aggregation(aggregationBuilder)
        val searchRequest = SearchRequest(Const.INDICES)
            .source(searchSourceBuilder)
        val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
        val aggregation: CompositeAggregation = searchResponse.aggregations[name]
        return buildResponse(aggregation)
    }

    fun queryTask(
        request: ElasticsearchQueryRequest,
        pageRequest: PageRequest
    ): Map<String, Any?> {
        val stringQueryBuilder = QueryBuilders
            .queryStringQuery(request.query)
            .defaultOperator(Operator.AND)
        val searchSourceBuilder = SearchSourceBuilder.searchSource()
            .size(pageRequest.size)
            .from(pageRequest.offset)
            .query(stringQueryBuilder)
        val searchRequest = SearchRequest(Const.INDICES)
            .source(searchSourceBuilder)
        val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
        return buildResponse(searchResponse.hits)
    }

    fun getTaskField(): Map<String, Any> {
        val mappingsRequest = GetMappingsRequest()
            .indices(Const.INDICES)
        val mappingsResponse = client.indices()
            .getMapping(mappingsRequest, RequestOptions.DEFAULT)
        return mappingsResponse.mappings().mapValues { it.value.sourceAsMap }
    }

    private fun buildResponse(hits: SearchHits) = mapOf(
        "totalHits" to hits.totalHits,
        "pages" to hits.hits.map { it.sourceAsMap }
    )

    private fun buildResponse(aggregation: CompositeAggregation) = mapOf(
        "afterKey" to aggregation.afterKey(),
        "pages" to aggregation.buckets.map { it.key }
    )
}
