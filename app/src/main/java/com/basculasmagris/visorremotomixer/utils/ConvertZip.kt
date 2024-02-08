package com.basculasmagris.visorremotomixer.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.io.IOException
class ConvertZip {
    fun compressText1(text: String):ByteArray{
        val outputStream = ByteArrayOutputStream()
        val gzipOutputStream = GZIPOutputStream(outputStream)
        gzipOutputStream.write(text.toByteArray())
        gzipOutputStream.close()
        return outputStream.toByteArray()
    }

    fun decompressText1(compressedData: ByteArray): String{
        val inputStream = ByteArrayInputStream(compressedData)
        val gzipInputStream = GZIPInputStream(inputStream)
        val buffer = ByteArray(16384)
        var decompressed = ""
        var lenght = gzipInputStream.read()
        while (lenght > 0){
            decompressed += String(buffer,0,lenght)
            lenght = gzipInputStream.read(buffer)
        }
        gzipInputStream.close()
        return decompressed
    }

    fun compressText(texto: String): ByteArray {
        val byteStream = ByteArrayOutputStream()
        try {
            val gzipStream = GZIPOutputStream(byteStream)
            gzipStream.write(texto.toByteArray())
            gzipStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return byteStream.toByteArray()
    }

    fun decompressText(compressedText: ByteArray): String {
        val byteStream = ByteArrayInputStream(compressedText)
        val output = ByteArrayOutputStream()
        try {
            val gzipStream = GZIPInputStream(byteStream)
            val buffer = ByteArray(16384)
            var len: Int
            while (gzipStream.read(buffer).also { len = it } > 0) {
                output.write(buffer, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return output.toString()
    }
}


