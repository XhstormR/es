package com.xhstormr.web.domain.util

import java.io.File

object TarArchiver {

    private const val ZIP_COMMAND =
        """tar --no-unquote --force-local -f %s -u %s"""

    private const val UNZIP_COMMAND =
        """tar --no-unquote --force-local -f %s -x %s"""

    fun zip(file: File): File {
        val archive = file.resolveSibling(file.name + ".tar")
        return zip(archive, listOf(file))
    }

    fun zip(archive: File, files: List<File>): File {
        files.forEach { require(it) }

        val paths = files.joinToString(" ", transform = transform)
        exec(ZIP_COMMAND.format(archive, paths))
        return archive
    }

    fun unzip(archive: File): File {
        require(archive)

        val archiveDir = archive.resolveSibling(archive.nameWithoutExtension)
            .apply { mkdirs() }
        exec(UNZIP_COMMAND.format(archive, transform(archiveDir)))
        return archiveDir
    }

    private val transform: (File) -> String = {
        if (it.isDirectory) "-C ${it.path} ./"
        else "-C ${it.parent} ./${it.name}"
    }
}
