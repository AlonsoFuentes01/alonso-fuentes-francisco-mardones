package com.example.aplicacion_escuela.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val HeaderBlue = Color(0xFF4A6CFF)
private val BodyGray = Color(0xFFE8E8E8)
private const val MIN_LARGO = 6

@Composable
fun GestionarPerfilScreen(
    // Retorna true si se cambió; false si la contraseña actual es incorrecta
    onChangePassword: (actual: String, nueva: String) -> Boolean,
    onBack: () -> Unit
) {
    var actual by remember { mutableStateOf("") }
    var nueva by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

    var actualError by remember { mutableStateOf<String?>(null) }
    var nuevaError by remember { mutableStateOf<String?>(null) }
    var confirmarError by remember { mutableStateOf<String?>(null) }
    var exito by remember { mutableStateOf(false) }

    fun guardar() {
        exito = false
        actualError = if (actual.isEmpty()) "Ingresa tu contraseña actual" else null
        nuevaError = when {
            nueva.length < MIN_LARGO -> "Mínimo $MIN_LARGO caracteres"
            nueva == actual -> "La nueva contraseña debe ser distinta a la actual"
            else -> null
        }
        confirmarError = if (confirmar != nueva) "Las contraseñas no coinciden" else null

        if (actualError != null || nuevaError != null || confirmarError != null) return

        if (onChangePassword(actual, nueva)) {
            actual = ""; nueva = ""; confirmar = ""
            exito = true
        } else {
            actualError = "La contraseña actual es incorrecta"
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Header con título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(HeaderBlue)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text("Gestionar Perfil", color = Color.White, fontSize = 20.sp)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(BodyGray)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Cambiar contraseña", fontSize = 18.sp, color = Color.Black)
            Spacer(Modifier.height(8.dp))

            PasswordField("Contraseña actual", actual, actualError) {
                actual = it; actualError = null; exito = false
            }
            PasswordField("Nueva contraseña", nueva, nuevaError) {
                nueva = it; nuevaError = null; exito = false
            }
            PasswordField("Confirmar nueva contraseña", confirmar, confirmarError) {
                confirmar = it; confirmarError = null; exito = false
            }

            if (exito) {
                Text(
                    "Contraseña actualizada correctamente",
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { guardar() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar contraseña")
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF4A5FBF))
        )
    }
}

@Composable
private fun PasswordField(
    label: String,
    value: String,
    error: String?,
    onChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            label = { Text(label) },
            visualTransformation = PasswordVisualTransformation(),
            isError = error != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}