package com.example.aplicacion_escuela.viewmodel

import com.example.aplicacion_escuela.data.InspectorApi
import com.example.aplicacion_escuela.data.TicketRevision
import com.example.aplicacion_escuela.data.VistaInspector
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class BandejaInspectorViewModelTest {
    private val reporte = TicketRevision(4, "Mobiliario", "Sala 2", "Mesa rota", "PENDIENTE", false)
    private val credencial = "Basic prueba"

    @Test
    fun cargar_usaLaCuentaActualEnLasTresVistas() = runBlocking {
        val rutas = mutableListOf<String>()
        val modelo = BandejaInspectorViewModel(api(listar = { autorizacion, ruta ->
            assertEquals(credencial, autorizacion)
            rutas += ruta
            listOf(reporte)
        }))

        VistaInspector.entries.forEach { modelo.cargar(it, credencial) }

        assertEquals(listOf("pendientes", "historial", "aceptados"), rutas)
        assertEquals(listOf(reporte), modelo.tickets)
    }

    @Test
    fun cargar_muestraElErrorDeLaBandeja() = runBlocking {
        val modelo = BandejaInspectorViewModel(api(listar = { _, _ ->
            throw HttpException(Response.error<List<TicketRevision>>(401, "".toResponseBody()))
        }))

        modelo.cargar(VistaInspector.PENDIENTES, credencial)

        assertFalse(modelo.cargado)
        assertEquals("El servidor rechazó el acceso a los tickets.", modelo.mensaje)
    }

    @Test
    fun aceptar_retiroElTicketSoloTrasConfirmacionDelServidor() = runBlocking {
        val modelo = BandejaInspectorViewModel(api(
            listar = { _, _ -> listOf(reporte) },
            aceptar = { _, numero -> assertEquals(4L, numero) }
        ))
        modelo.cargar(VistaInspector.PENDIENTES, credencial)
        modelo.aceptar(4, credencial)

        assertTrue(modelo.tickets.isEmpty())
        assertEquals("Ticket #4 aceptado. Estado REVISADO.", modelo.mensaje)
    }

    private fun api(
        listar: (String, String) -> List<TicketRevision>,
        aceptar: (String, Long) -> Unit = { _, _ -> error("Aceptación inesperada") }
    ): InspectorApi = object : InspectorApi {
        override suspend fun listar(autorizacion: String, vista: String) = listar.invoke(autorizacion, vista)
        override suspend fun aceptar(autorizacion: String, numero: Long) =
            aceptar.invoke(autorizacion, numero)
    }
}
