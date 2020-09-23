package com.xhstormr.web.domain.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

/**
 * 内联：for convenience
 */
inline fun getLogger(): Logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

inline fun <reified T> clazz() = T::class.java
