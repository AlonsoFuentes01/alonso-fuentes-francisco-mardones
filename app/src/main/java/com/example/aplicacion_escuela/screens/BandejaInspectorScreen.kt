package com.example.aplicacion_escuela.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacion_escuela.data.VistaInspector
import com.example.aplicacion_escuela.viewmodel.BandejaInspectorViewModel
import kotlinx.coroutines.launch

@Composable
fun BandejaInspectorScreen(
    vista: VistaInspector,
    onVolver: () -> Unit,
    autorizacion: String,
    modelo: BandejaInspectorViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(vista, autorizacion) { modelo.cargar(vista, autorizacion) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            TextButton(onClick = onVolver) { Text("Volver") }
            Text(vista.titulo, style = MaterialTheme.typography.headlineMedium)
            TextButton(
                onClick = { scope.launch { modelo.cargar(vista, autorizacion) } },
                enabled = !modelo.ocupado
            ) { Text("Actualizar") }
        }

        if (modelo.ocupado) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
        modelo.mensaje?.let { mensaje -> item { Text(mensaje) } }

        if (modelo.cargado && modelo.tickets.isEmpty() && !modelo.ocupado) {
            item {
                Text(
                    when (vista) {
                        VistaInspector.PENDIENTES -> "No hay tickets pendientes."
                        VistaInspector.HISTORIAL -> "Todavía no hay tickets."
                        VistaInspector.ACEPTADOS -> "Todavía no hay tickets aceptados."
                    }
                )
            }
        }

        items(modelo.tickets, key = { it.numero }) { ticket ->
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Ticket #${ticket.numero}", style = MaterialTheme.typography.titleLarge)
                    Text("${ticket.categoria} · ${ticket.ubicacion}")
                    Text(ticket.descripcion)
                    Text(ticket.estado, color = MaterialTheme.colorScheme.primary)
                    if (ticket.fotoAdjunta) Text("Foto adjunta")
                    if (vista == VistaInspector.PENDIENTES) {
                        Button(
                            onClick = { scope.launch { modelo.aceptar(ticket.numero, autorizacion) } },
                            enabled = !modelo.ocupado
                        ) { Text("Aceptar ticket") }
                    }
                }
            }
        }
    }
}
