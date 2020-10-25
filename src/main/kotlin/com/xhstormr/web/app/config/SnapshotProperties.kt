package com.xhstormr.web.app.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.io.File

/**
 * @author zhangzf
 * @create 2018/11/5 21:18
 */
@ConstructorBinding
@ConfigurationProperties("snapshot")
class SnapshotProperties(
    val dir: File,
) {
    init {
        dir.mkdirs()
    }
}
