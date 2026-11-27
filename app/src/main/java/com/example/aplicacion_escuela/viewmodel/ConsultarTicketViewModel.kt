package com.example.aplicacion_escuela.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.aplicacion_escuela.data.TicketApi
import com.example.aplicacion_escuela.data.TicketApiClient
import com.example.aplicacion_escuela.data.TicketCreado
import java.io.IOException
import retrofit2.HttpException

class ConsultarTicketViewModel(
    private val api: TicketApi = TicketApiClient.api
) : ViewModel() {
    var consultando by mutableStateOf(false)
        private set
    var errorNumero by mutableStateOf<String?>(null)
        private set
    var errorConsulta by mutableStateOf<String?>(null)
        private set
    var ticket by mutableStateOf<TicketCreado?>(null)
        private set

    suspend fun consultar(numeroIngresado: String) {
        if (consultando) return

        ticket = null
        errorNumero = null
        errorConsulta = null
        val numero = numeroIngresado.trim().toLongOrNull()
        if (numero == null || numero <= 0) {
            errorNumero = "Ingresa un número de ticket válido."
            return
        }

        consultando = true
        try {
            ticket = api.buscarTicket(numero)
        } catch (error: HttpException) {
            errorConsulta = if (error.code() == 404) {
                "No encontramos el ticket #$numero."
            } else {
                "El servidor no pudo completar la consulta."
            }
        } catch (_: IOException) {
            errorConsulta = "Revisa tu conexión e intenta de nuevo."
        } finally {
            consultando = false
        }
    }
}
