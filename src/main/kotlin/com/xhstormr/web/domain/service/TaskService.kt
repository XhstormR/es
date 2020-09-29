package com.xhstormr.web.domain.service

import com.xhstormr.web.app.config.SnapshotProperties
import com.xhstormr.web.domain.util.Archiver
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties

@Service
class TaskService(
    private val elasticsearchSharedService: ElasticsearchSharedService,
    private val snapshotProperties: SnapshotProperties
) {

    fun importTask(file: File) {
        require(file.exists()) { "文件不存在" }

        val dir = Archiver.unzip(file)

        val properties = Properties()
            .apply { dir.resolve(snapshotProperties.manifest).bufferedReader().use { load(it) } }

        val taskIndex = properties["taskIndex"]!!.toString()

        elasticsearchSharedService.importIndex(taskIndex, dir.resolve("$taskIndex.json"))
        dir.deleteRecursively()
    }

    fun exportTask(taskIndex: String): File {
        require(elasticsearchSharedService.existsIndex(taskIndex)) { "索引不存在" }

        val prefix = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDateTime.now())
        val dir = snapshotProperties.dir.resolve("$prefix-$taskIndex")
            .apply { mkdirs() }

        elasticsearchSharedService.dumpIndex(taskIndex, dir.resolve("$taskIndex.json"))

        Properties()
            .apply { this["taskIndex"] = taskIndex }
            .run { dir.resolve(snapshotProperties.manifest).bufferedWriter().use { store(it, null) } }

        val file = Archiver.zip(dir)
        dir.deleteRecursively()
        return file
    }
}
