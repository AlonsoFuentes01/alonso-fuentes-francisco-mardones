package com.example.aplicacion_escuela.viewmodel

import com.example.aplicacion_escuela.data.CrearTicketRequest
import com.example.aplicacion_escuela.data.TicketApi
import com.example.aplicacion_escuela.data.TicketCreado
import java.io.IOException
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ConsultarTicketViewModelTest {
    @Test
    fun consultar_muestraEstadoYFotoDelTicket() = runBlocking {
        val modelo = ConsultarTicketViewModel(api { TicketCreado(it, "PENDIENTE", true) })

        modelo.consultar("12")

        assertEquals(TicketCreado(12, "PENDIENTE", true), modelo.ticket)
        assertFalse(modelo.consultando)
    }

    @Test
    fun consultar_rechazaNumeroInvalidoSinLlamarALaApi() = runBlocking {
        val modelo = ConsultarTicketViewModel(api { error("Consulta inesperada") })

        modelo.consultar("0")

        assertEquals("Ingresa un número de ticket válido.", modelo.errorNumero)
    }

    @Test
    fun consultar_muestraMensajeSiElTicketNoExiste() = runBlocking {
        val modelo = ConsultarTicketViewModel(api {
            throw HttpException(Response.error<TicketCreado>(404, "".toResponseBody()))
        })

        modelo.consultar("12")

        assertEquals("No encontramos el ticket #12.", modelo.errorConsulta)
        assertNull(modelo.ticket)
    }

    @Test
    fun consultar_permiteReintentarSiFallaLaConexion() = runBlocking {
        var consultas = 0
        val modelo = ConsultarTicketViewModel(api {
            consultas += 1
            if (consultas == 1) throw IOException()
            TicketCreado(it, "PENDIENTE")
        })

        modelo.consultar("12")
        assertEquals("Revisa tu conexión e intenta de nuevo.", modelo.errorConsulta)

        modelo.consultar("12")
        assertEquals(TicketCreado(12, "PENDIENTE"), modelo.ticket)
        assertNull(modelo.errorConsulta)
    }

    private fun api(buscar: (Long) -> TicketCreado): TicketApi = object : TicketApi {
        override suspend fun buscarTicket(numero: Long): TicketCreado = buscar(numero)
        override suspend fun crearTicket(solicitud: CrearTicketRequest): TicketCreado =
            error("Creación inesperada")
    }
}
