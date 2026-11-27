package com.example.aplicacion_escuela.data

import java.io.InputStream
import java.util.Base64

data class FotoAdjunta(val base64: String) {
    companion object {
        const val MAX_BYTES = 2 * 1024 * 1024

        fun leer(entrada: InputStream): FotoAdjunta {
            val bytes = entrada.readNBytes(MAX_BYTES + 1)
            require(bytes.size <= MAX_BYTES) { "La foto supera 2 MB." }
            val jpeg = bytes.size >= 3 &&
                bytes[0] == 0xff.toByte() && bytes[1] == 0xd8.toByte() && bytes[2] == 0xff.toByte()
            val firmaPng = byteArrayOf(0x89.toByte(), 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a)
            val png = bytes.size >= firmaPng.size &&
                firmaPng.indices.all { bytes[it] == firmaPng[it] }
            require(jpeg || png) { "Selecciona una foto JPG o PNG." }
            return FotoAdjunta(Base64.getEncoder().encodeToString(bytes))
        }
    }
}
