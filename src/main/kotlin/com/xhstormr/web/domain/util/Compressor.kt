package com.xhstormr.web.domain.util

import java.io.File

object Compressor {

    private const val KEY = """ae54fb21-6259-424e-8ec8-21deac58dc8b"""

    private const val COMPRESS_COMMAND =
        """zstd -c %s | openssl enc -aes-256-ctr -k $KEY -pbkdf2 -e -out %s"""

    private const val DECOMPRESS_COMMAND =
        """openssl enc -aes-256-ctr -k $KEY -pbkdf2 -d -in %s | zstd -d -o %s"""

    fun compress(file: File): File {
        val ret = file.resolveSibling(file.name + ".enc")
        exec(COMPRESS_COMMAND.format(file, ret))
        return ret
    }

    fun decompress(file: File): File {
        val ret = file.resolveSibling(file.name + ".dec")
        exec(DECOMPRESS_COMMAND.format(file, ret))
        return ret
    }
}
