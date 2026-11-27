package com.example.aplicacion_escuela.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        modelo.adjuntarFoto(uri = it)
    }
    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        modelo.adjuntarFoto(captura = it)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier.imePadding().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(onClick = onVolver, enabled = !modelo.enviando) { Text("Volver") }

            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Nuevo ticket", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        "Reporta un problema de infraestructura, mobiliario o tecnología.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Datos del problema", style = MaterialTheme.typography.titleMedium)
                    CampoTicket("Categoría", modelo.categoria, { modelo.actualizarBorrador(categoria = it) }, modelo)
                    CampoTicket("Ubicación", modelo.ubicacion, { modelo.actualizarBorrador(ubicacion = it) }, modelo)
                    CampoTicket("Descripción", modelo.descripcion, { modelo.actualizarBorrador(descripcion = it) }, modelo, false)
                    Text("El texto se guarda como borrador.", style = MaterialTheme.typography.bodySmall)
                }
            }

            Card {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Foto opcional", style = MaterialTheme.typography.titleMedium)
                    Text("JPG o PNG · hasta 2 MB", style = MaterialTheme.typography.bodySmall)
                    val permiteFoto = !modelo.enviando && !modelo.cargandoFoto
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { camara.launch(null) },
                            enabled = permiteFoto,
                            modifier = Modifier.weight(1f)
                        ) { Text("Cámara") }
                        OutlinedButton(
                            onClick = { galeria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                            enabled = permiteFoto,
                            modifier = Modifier.weight(1f)
                        ) { Text("Galería") }
                    }
                    if (modelo.cargandoFoto) LinearProgressIndicator(Modifier.fillMaxWidth())
                    if (modelo.foto != null) {
                        Text("Foto lista para enviar")
                        TextButton(onClick = modelo::quitarFoto, enabled = permiteFoto) { Text("Quitar foto") }
                    }
                    modelo.errorFoto?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            }

            Button(
                onClick = modelo::enviar,
                enabled = !modelo.enviando && !modelo.cargandoFoto,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (modelo.enviando) "Enviando…" else "Enviar ticket")
            }

            modelo.ticket?.let { ticket ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Ticket #${ticket.numero}", style = MaterialTheme.typography.titleLarge)
                        Text(if (ticket.fotoAdjunta) "${ticket.estado} · Foto guardada" else ticket.estado)
                    }
                }
            }
            modelo.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

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
            modelo.errorClima?.let { Text(it, color = MaterialTheme.colorScheme.error) }
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
        supportingText = error?.let { mensaje -> { Text(mensaje) } },
        singleLine = unaLinea,
        minLines = if (unaLinea) 1 else 3,
        enabled = !modelo.enviando,
        modifier = Modifier.fillMaxWidth()
    )
}
