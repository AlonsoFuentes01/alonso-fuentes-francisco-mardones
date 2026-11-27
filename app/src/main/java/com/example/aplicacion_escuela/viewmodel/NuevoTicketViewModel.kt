package com.example.aplicacion_escuela.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion_escuela.data.CrearTicketRequest
import com.example.aplicacion_escuela.data.TicketApiClient
import com.example.aplicacion_escuela.data.TicketCreado
import java.io.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NuevoTicketViewModel : ViewModel() {
    var categoria by mutableStateOf("")
    var ubicacion by mutableStateOf("")
    var descripcion by mutableStateOf("")
    var enviando by mutableStateOf(false)
        private set
    var intentoEnvio by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var ticket by mutableStateOf<TicketCreado?>(null)
        private set

    fun enviar() {
        if (enviando) return

        intentoEnvio = true
        error = null
        ticket = null
        val campos = listOf(categoria, ubicacion, descripcion)
        if (campos.any { it.isBlank() || it.length > 255 }) return

        enviando = true
        val solicitud = CrearTicketRequest(categoria.trim(), ubicacion.trim(), descripcion.trim())
        viewModelScope.launch {
            try {
                ticket = TicketApiClient.api.crearTicket(solicitud)
                intentoEnvio = false
                categoria = ""
                ubicacion = ""
                descripcion = ""
            } catch (_: IOException) {
                error = "No se pudo confirmar el envío. Revisa la conexión."
            } catch (_: HttpException) {
                error = "El servidor no pudo procesar el reporte. Intenta más tarde."
            } finally {
                enviando = false
            }
        }
    }
}
