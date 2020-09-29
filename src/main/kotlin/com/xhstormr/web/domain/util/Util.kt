package com.xhstormr.web.domain.util

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
        Runtime.getRuntime().exec("""cmd /c %s""".format(command)).waitFor()
    else
        Runtime.getRuntime().exec("""sh -c %s""".format(command)).waitFor()
