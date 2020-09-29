package com.xhstormr.web.domain.util

import java.io.File

object Archiver {

    private const val KEY = """ae54fb21-6259-424e-8ec8-21deac58dc8b"""

    private const val ZIP_COMMAND =
        """7zr a -y -p$KEY -mhe -mx4 -sdel %s %s/*"""

    private const val UNZIP_COMMAND =
        """7zr x -y -p$KEY -o%s %s"""

    fun zip(file: File): File {
        val ret = file.resolveSibling(file.name + ".7z")
        exec(ZIP_COMMAND.format(ret, file))
        return ret
    }

    fun unzip(file: File): File {
        val ret = file.resolveSibling(file.nameWithoutExtension)
        exec(UNZIP_COMMAND.format(ret, file))
        return ret
    }
}
