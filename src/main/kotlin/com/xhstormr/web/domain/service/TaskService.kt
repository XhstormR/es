package com.xhstormr.web.domain.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.xhstormr.web.app.config.SnapshotProperties
import com.xhstormr.web.domain.model.SnapshotManifest
import com.xhstormr.web.domain.model.request.PageRequest
import com.xhstormr.web.domain.model.request.TaskExportRequest
import com.xhstormr.web.domain.model.response.TaskImportResponse
import com.xhstormr.web.domain.util.Archiver
import com.xhstormr.web.domain.util.clazz
import com.xhstormr.web.domain.util.require
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class TaskService(
    private val elasticsearchSharedService: ElasticsearchSharedService,
    private val snapshotProperties: SnapshotProperties,
    private val objectMapper: ObjectMapper
) {

    companion object {
        const val MANIFEST_FILE = "MANIFEST.properties"

        const val METADATA_FILE = "METADATA.json"
        const val METADATA_INDICES = "metadata.task"
        const val METADATA_QUERY = "task:%s"
    }

    fun importTask(file: File): TaskImportResponse {
        require(file)

        val archiveDir = Archiver.unarchive(file)

        val (indices, videos, taskId) = objectMapper.readValue(
            archiveDir.resolve(MANIFEST_FILE),
            clazz<SnapshotManifest>()
        )

        indices.forEach { elasticsearchSharedService.importIndex(it, archiveDir.resolve("$it.json")) }

        elasticsearchSharedService.importIndex(METADATA_INDICES, archiveDir.resolve(METADATA_FILE), true)

        return TaskImportResponse(videos.map { archiveDir.resolve(it) }, taskId)
    }

    fun exportTask(request: TaskExportRequest): File {
        val (indices, videos, taskId) = request
        indices.forEach { require(elasticsearchSharedService.existsIndex(it)) { "索引不存在" } }
        videos.forEach { require(it) }

        val archiveName = "${DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDateTime.now())}-${UUID.randomUUID()}"
        val archiveDir = snapshotProperties.dir.resolve(archiveName)
            .apply { mkdirs() }

        val metadata = elasticsearchSharedService.stringQuery(
            METADATA_QUERY.format(taskId),
            METADATA_INDICES,
            PageRequest()
        ).map { it.sourceAsMap }[0]
        val manifest = SnapshotManifest(indices, videos.map { it.name }, taskId)

        objectMapper.writeValue(archiveDir.resolve(METADATA_FILE), metadata)
        objectMapper.writeValue(archiveDir.resolve(MANIFEST_FILE), manifest)

        indices.forEach { elasticsearchSharedService.dumpIndex(it, archiveDir.resolve("$it.json")) }

        return Archiver.archive(archiveDir, videos)
    }
}
