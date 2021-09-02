package asm

import java.io.File

/**
 * @author archko 2021/8/30:19:02
 */
object FileUtils {
    fun writeByteArrayToFile(file: File, toByteArray: ByteArray) {
        file.writeBytes(toByteArray)
    }
}