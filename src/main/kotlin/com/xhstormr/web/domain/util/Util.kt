package com.xhstormr.web.domain.util

import org.elasticsearch.search.SearchHits
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.invoke.MethodHandles

/**
 * 内联：for convenience
 */
inline fun getLogger(): Logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

inline fun <reified T> clazz() = T::class.java

fun isWindows() = File.separatorChar == '\\'

fun exec(command: String) =
    if (isWindows())
        Runtime.getRuntime().exec(arrayOf("cmd", "/c", command)).waitFor()
    else
        Runtime.getRuntime().exec(arrayOf("sh", "-c", command)).waitFor()

fun require(file: File) =
    require(file.exists()) { "$file 文件不存在" }

fun SearchHits.toResponse() = mapOf(
    "totalHits" to totalHits,
    "pages" to hits.map { it.sourceAsMap }
)
