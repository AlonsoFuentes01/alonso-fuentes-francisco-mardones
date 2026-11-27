package com.example.aplicacion_escuela.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacion_escuela.viewmodel.RegisterViewModel

@Composable
fun RegisterForm(
    onRegistered: () -> Unit,
    vm: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { vm.onFileSelected(it) }
    }

    val mimeTypes = arrayOf(
        "application/pdf",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/msword"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        OutlinedTextField(
            value = vm.email,
            onValueChange = { vm.email = it },
            label = { Text("Correo institucional (@escuela.cl)") },
            isError = vm.emailError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        ErrorText(vm.emailError)

        OutlinedTextField(
            value = vm.password,
            onValueChange = { vm.password = it },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "Ocultar" else "Mostrar", fontSize = 13.sp)
                }
            },
            isError = vm.passwordError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        ErrorText(vm.passwordError)

        val hayArchivo = vm.fileName.isNotEmpty()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                .clickable { picker.launch(mimeTypes) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (hayArchivo) vm.fileName
                else "Toca aquí para subir el archivo que valida que eres docente (PDF o Word)",
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                // Deja espacio a ambos lados para que no choque con la X
                modifier = Modifier.padding(horizontal = if (hayArchivo) 44.dp else 8.dp)
            )

            if (hayArchivo) {
                Text(
                    text = "✕",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { vm.clearFile() }
                        .padding(16.dp)
                )
            }
        }
        ErrorText(vm.fileError)

        Button(
            onClick = {
                if (vm.register()) {
                    Toast.makeText(context, "Cuenta creada", Toast.LENGTH_SHORT).show()
                    onRegistered()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarme")
        }
    }
}

@Composable
private fun ErrorText(error: String?) {
    AnimatedVisibility(visible = error != null) {
        Text(
            text = error ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}