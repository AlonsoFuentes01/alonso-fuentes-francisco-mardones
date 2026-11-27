package com.example.aplicacion_escuela.data

import java.io.ByteArrayInputStream
import org.junit.Assert.assertEquals
import org.junit.Test

class FotoAdjuntaTest {
    @Test
    fun leer_codificaJpegComoBase64() {
        val bytes = byteArrayOf(0xff.toByte(), 0xd8.toByte(), 0xff.toByte(), 0xd9.toByte())
        assertEquals("/9j/2Q==", FotoAdjunta.leer(ByteArrayInputStream(bytes)).base64)
    }

    @Test(expected = IllegalArgumentException::class)
    fun leer_rechazaArchivoQueNoEsFoto() {
        FotoAdjunta.leer(ByteArrayInputStream("texto".toByteArray()))
    }

    @Test(expected = IllegalArgumentException::class)
    fun leer_rechazaFotoQueSuperaElLimite() {
        FotoAdjunta.leer(ByteArrayInputStream(ByteArray(FotoAdjunta.MAX_BYTES + 1)))
    }
}
