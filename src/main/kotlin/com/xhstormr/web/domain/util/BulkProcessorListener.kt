package com.xhstormr.web.domain.util

import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse

object BulkProcessorListener : BulkProcessor.Listener {

    private val LOGGER = getLogger()

    override fun beforeBulk(executionId: Long, request: BulkRequest) {
        LOGGER.debug("Executing bulk [{}] with {} requests", executionId, request.numberOfActions())
    }

    override fun afterBulk(executionId: Long, request: BulkRequest, response: BulkResponse) {
        if (response.hasFailures()) {
            LOGGER.error("Bulk [{}] executed with failures", executionId)
        } else {
            LOGGER.debug("Bulk [{}] completed in {} milliseconds", executionId, response.took.millis)
        }
    }

    override fun afterBulk(executionId: Long, request: BulkRequest, failure: Throwable) {
        LOGGER.error("Failed to execute bulk", failure)
    }
}
