package com.xhstormr.web.domain.util

import java.io.File

object Archiver {

    fun archive(file: File, extra: List<File> = emptyList()) = TarArchiver.zip(file)
        .apply { TarArchiver.zip(this, extra) }
        .run { ZstdCompressor.compress(this).also { delete() } }
        .run { OpenSSLEncryptor.encrypt(this).also { delete() } }
        .also { file.deleteRecursively() }

    fun unarchive(file: File) = OpenSSLEncryptor.decrypt(file)
        .run { ZstdCompressor.decompress(this).also { delete() } }
        .run { TarArchiver.unzip(this).also { delete() } }
        .also { file.delete() }
}
