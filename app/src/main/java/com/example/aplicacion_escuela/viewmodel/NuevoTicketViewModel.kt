package com.example.aplicacion_escuela.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion_escuela.data.CrearTicketRequest
import com.example.aplicacion_escuela.data.TicketApiClient
import com.example.aplicacion_escuela.data.TicketCreado
import com.example.aplicacion_escuela.data.UserRepository
import java.io.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NuevoTicketViewModel(app: Application) : AndroidViewModel(app) {
    private val cuenta = UserRepository(app).getSession()
    private val borrador = cuenta?.let {
        app.getSharedPreferences("borrador_ticket_${Uri.encode(it)}", Context.MODE_PRIVATE)
    }

    var categoria by mutableStateOf(borrador?.getString("categoria", "").orEmpty())
        private set
    var ubicacion by mutableStateOf(borrador?.getString("ubicacion", "").orEmpty())
        private set
    var descripcion by mutableStateOf(borrador?.getString("descripcion", "").orEmpty())
        private set
    var enviando by mutableStateOf(false)
        private set
    var intentoEnvio by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var ticket by mutableStateOf<TicketCreado?>(null)
        private set

    fun actualizarBorrador(
        categoria: String = this.categoria,
        ubicacion: String = this.ubicacion,
        descripcion: String = this.descripcion
    ) {
        this.categoria = categoria
        this.ubicacion = ubicacion
        this.descripcion = descripcion
        borrador?.edit()
            ?.putString("categoria", categoria)
            ?.putString("ubicacion", ubicacion)
            ?.putString("descripcion", descripcion)
            ?.apply()
    }

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
                borrador?.edit()?.clear()?.apply()
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
