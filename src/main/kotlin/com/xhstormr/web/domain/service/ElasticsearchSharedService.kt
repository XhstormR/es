package com.xhstormr.web.domain.service

import com.xhstormr.web.domain.model.request.PageRequest
import com.xhstormr.web.domain.util.BulkProcessorListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.ClearScrollRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchScrollRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.GetIndexRequest
import org.elasticsearch.client.indices.GetMappingsRequest
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHits
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption

@Service
class ElasticsearchSharedService(
    private val client: RestHighLevelClient
) {

    fun stringQuery(
        query: String,
        indices: String,
        pageRequest: PageRequest
    ): SearchHits {
        val stringQueryBuilder = QueryBuilders
            .queryStringQuery(query)
            .defaultOperator(Operator.AND)
        val searchSourceBuilder = SearchSourceBuilder.searchSource()
            .size(pageRequest.size)
            .from(pageRequest.offset)
            .query(stringQueryBuilder)
        val searchRequest = SearchRequest(indices)
            .source(searchSourceBuilder)
        val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
        return searchResponse.hits
    }

    fun getIndex(indices: String): Array<String> {
        val indexRequest = GetIndexRequest(indices)
        val indexResponse = client.indices()
            .get(indexRequest, RequestOptions.DEFAULT)
        return indexResponse.indices
    }

    fun deleteIndex(indices: String): Boolean {
        val deleteIndexRequest = DeleteIndexRequest(indices)
        val deleteIndexResponse = client.indices()
            .delete(deleteIndexRequest, RequestOptions.DEFAULT)
        return deleteIndexResponse.isAcknowledged
    }

    fun existsIndex(indices: String): Boolean {
        val indexRequest = GetIndexRequest(indices)
        return client.indices()
            .exists(indexRequest, RequestOptions.DEFAULT)
    }

    fun getMappings(indices: String): Map<String, Any> {
        val mappingsRequest = GetMappingsRequest()
            .indices(indices)
        val mappingsResponse = client.indices()
            .getMapping(mappingsRequest, RequestOptions.DEFAULT)
        return mappingsResponse.mappings().mapValues { it.value.sourceAsMap }
    }

    fun dumpIndex(indices: String, file: File) {
        runBlocking {
            val channel = produce(Dispatchers.IO, Channel.UNLIMITED) {
                val searchSourceBuilder = SearchSourceBuilder.searchSource()
                    .size(3000)
                val searchRequest = SearchRequest(indices)
                    .source(searchSourceBuilder)
                    .scroll(TimeValue.timeValueMinutes(1L))
                var searchResponse = client
                    .search(searchRequest, RequestOptions.DEFAULT)
                var scrollId = searchResponse.scrollId
                var searchHits = searchResponse.hits.hits

                do {
                    send(searchHits)
                    val scrollRequest = SearchScrollRequest(scrollId)
                        .scroll(TimeValue.timeValueMinutes(1L))
                    searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT)
                    scrollId = searchResponse.scrollId
                    searchHits = searchResponse.hits.hits
                } while (searchHits.isNotEmpty())

                val clearScrollRequest = ClearScrollRequest()
                    .apply { addScrollId(scrollId) }
                val clearScrollResponse = client
                    .clearScroll(clearScrollRequest, RequestOptions.DEFAULT)
            }

            Files.newByteChannel(
                file.toPath(),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
            ).use {
                channel
                    .consumeAsFlow()
                    .flatMapConcat { it.asFlow() }
                    .map { it.sourceAsString }
                    .collect { searchHit -> it.write(Charsets.UTF_8.encode(searchHit + System.lineSeparator())) }
            }
        }
    }

    fun importIndex(indices: String, file: File, append: Boolean = false) {
        if (!append && existsIndex(indices)) deleteIndex(indices)

        BulkProcessor.builder(
            { request, bulkListener -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener) },
            BulkProcessorListener
        )
            .setBulkActions(3000)
            .setConcurrentRequests(16)
            .build().use { bulkProcessor ->
                Files.lines(file.toPath()).parallel().use { lines ->
                    lines
                        .map { IndexRequest(indices).source(it, XContentType.JSON) }
                        .forEach { bulkProcessor.add(it) }
                }
            }
    }
}
