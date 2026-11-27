package com.example.aplicacion_escuela.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.aplicacion_escuela.data.InspectorApi
import com.example.aplicacion_escuela.data.TicketApiClient
import com.example.aplicacion_escuela.data.TicketRevision
import com.example.aplicacion_escuela.data.VistaInspector
import java.io.IOException
import retrofit2.HttpException

class BandejaInspectorViewModel(
    private val api: InspectorApi = TicketApiClient.inspector
) : ViewModel() {
    var tickets by mutableStateOf(emptyList<TicketRevision>())
        private set
    var ocupado by mutableStateOf(false)
        private set
    var cargado by mutableStateOf(false)
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    suspend fun cargar(vista: VistaInspector, autorizacion: String) {
        operar {
            tickets = api.listar(autorizacion, vista.ruta)
            cargado = true
        }
    }

    suspend fun aceptar(numero: Long, autorizacion: String) {
        operar {
            api.aceptar(autorizacion, numero)
            tickets = tickets.filterNot { it.numero == numero }
            mensaje = "Ticket #$numero aceptado. Estado REVISADO."
        }
    }

    private suspend fun operar(accion: suspend () -> Unit) {
        if (ocupado) return

        ocupado = true
        mensaje = null
        try {
            accion()
        } catch (error: HttpException) {
            mensaje = when (error.code()) {
                401, 403 -> "El servidor rechazó el acceso a los tickets."
                404 -> "El ticket o la vista no están disponibles."
                409 -> "Este ticket ya fue revisado. Actualiza la lista."
                else -> "El servidor no pudo completar la operación."
            }
        } catch (_: IOException) {
            mensaje = "Revisa tu conexión e intenta de nuevo."
        } finally {
            ocupado = false
        }
    }
}
