package com.example.aplicacion_escuela.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacion_escuela.viewmodel.ConsultarTicketViewModel
import kotlinx.coroutines.launch

@Composable
fun ConsultarTicketScreen(
    onVolver: () -> Unit,
    modelo: ConsultarTicketViewModel = viewModel()
) {
    var numero by rememberSaveable { mutableStateOf("") }
    val consultas = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.imePadding().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(onClick = onVolver) { Text("Volver") }

            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Consultar ticket", style = MaterialTheme.typography.headlineMedium)
                    Text("Usa el número que recibiste al enviar tu reporte.")
                }
            }

            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text("Número de ticket") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                enabled = !modelo.consultando,
                isError = modelo.errorNumero != null,
                supportingText = modelo.errorNumero?.let { mensaje -> { Text(mensaje) } },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { consultas.launch { modelo.consultar(numero) } },
                enabled = !modelo.consultando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (modelo.consultando) "Consultando…" else "Consultar estado")
            }

            if (modelo.consultando) LinearProgressIndicator(Modifier.fillMaxWidth())

            modelo.ticket?.let { ticket ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                    Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Ticket #${ticket.numero}", style = MaterialTheme.typography.titleLarge)
                        Text(ticket.estado)
                        if (ticket.fotoAdjunta) Text("Foto adjunta")
                    }
                }
            }
            modelo.errorConsulta?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}
