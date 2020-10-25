package com.xhstormr.web.domain.util

import java.io.File

object OpenSSLEncryptor {

    private const val KEY =
        """ae54fb21-6259-424e-8ec8-21deac58dc8b"""

    private const val ENCRYPT_COMMAND =
        """openssl enc -aes-256-ctr -k $KEY -pbkdf2 -in %s -out %s -e"""

    private const val DECRYPT_COMMAND =
        """openssl enc -aes-256-ctr -k $KEY -pbkdf2 -in %s -out %s -d"""

    fun encrypt(file: File): File {
        require(file)

        val ret = file.resolveSibling(file.name + ".enc")
        exec(ENCRYPT_COMMAND.format(file, ret))
        return ret
    }

    fun decrypt(file: File): File {
        require(file)

        val ret = file.resolveSibling(file.nameWithoutExtension)
        exec(DECRYPT_COMMAND.format(file, ret))
        return ret
    }
}
