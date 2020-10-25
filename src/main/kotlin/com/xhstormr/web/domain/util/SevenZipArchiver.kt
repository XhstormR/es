package com.xhstormr.web.domain.util

import java.io.File

object SevenZipArchiver {

    private const val KEY =
        """ae54fb21-6259-424e-8ec8-21deac58dc8b"""

    private const val ZIP_COMMAND =
        """7zr a -y -p$KEY -mhe -mx4 -sdel %s %s"""

    private const val UNZIP_COMMAND =
        """7zr x -y -p$KEY -o%s %s"""

    fun zip(file: File): File {
        val archive = file.resolveSibling(file.name + ".7z")
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
        exec(UNZIP_COMMAND.format(archiveDir, archive))
        return archiveDir
    }

    private val transform: (File) -> String = {
        if (it.isDirectory) it.path + "/*"
        else it.path
    }
}
