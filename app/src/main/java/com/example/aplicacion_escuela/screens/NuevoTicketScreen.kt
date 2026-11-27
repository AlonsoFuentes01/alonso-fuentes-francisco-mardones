package com.example.aplicacion_escuela.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacion_escuela.viewmodel.NuevoTicketViewModel

@Composable
fun NuevoTicketScreen(
    onVolver: () -> Unit,
    modelo: NuevoTicketViewModel = viewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize().imePadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onVolver, enabled = !modelo.enviando) { Text("Volver") }
        Text("Nuevo ticket", style = MaterialTheme.typography.headlineMedium)
        Text("El borrador se guarda en este dispositivo.", style = MaterialTheme.typography.bodySmall)

        CampoTicket("Categoría", modelo.categoria, { modelo.actualizarBorrador(categoria = it) }, modelo)
        CampoTicket("Ubicación", modelo.ubicacion, { modelo.actualizarBorrador(ubicacion = it) }, modelo)
        CampoTicket("Descripción", modelo.descripcion, { modelo.actualizarBorrador(descripcion = it) }, modelo, false)

        Button(
            onClick = modelo::enviar,
            enabled = !modelo.enviando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (modelo.enviando) "Enviando…" else "Enviar ticket")
        }

        modelo.ticket?.let { ticket ->
            Text("Ticket #${ticket.numero} · ${ticket.estado}")
        }
        modelo.error?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        TextButton(onClick = modelo::consultarClima, enabled = !modelo.consultandoClima) {
            Text(if (modelo.consultandoClima) "Consultando clima…" else "Ver clima en Santiago")
        }
        modelo.clima?.let { clima ->
            Text(
                "Santiago · ${clima.temperatura} °C · Viento ${clima.velocidadViento} km/h",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("Datos de Open-Meteo", style = MaterialTheme.typography.bodySmall)
        }
        modelo.errorClima?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun CampoTicket(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    modelo: NuevoTicketViewModel,
    unaLinea: Boolean = true
) {
    val error = when {
        valor.length > 255 -> "Máximo 255 caracteres"
        modelo.intentoEnvio && valor.isBlank() -> "Completa este campo"
        else -> null
    }
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        singleLine = unaLinea,
        enabled = !modelo.enviando,
        modifier = Modifier.fillMaxWidth()
    )
}
