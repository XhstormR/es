package com.xhstormr.web.domain.util

import java.io.File

object ZstdCompressor {

    private const val COMPRESS_COMMAND =
        """zstd %s -o %s -f"""

    private const val DECOMPRESS_COMMAND =
        """zstd %s -o %s -f -d"""

    fun compress(file: File): File {
        require(file)

        val ret = file.resolveSibling(file.name + ".zst")
        exec(COMPRESS_COMMAND.format(file, ret))
        return ret
    }

    fun decompress(file: File): File {
        require(file)

        val ret = file.resolveSibling(file.nameWithoutExtension)
        exec(DECOMPRESS_COMMAND.format(file, ret))
        return ret
    }
}
