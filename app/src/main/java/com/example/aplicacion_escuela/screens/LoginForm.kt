package com.example.aplicacion_escuela.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacion_escuela.viewmodel.LoginViewModel

@Composable
fun LoginForm(
    onLoggedIn: () -> Unit,
    vm: LoginViewModel = viewModel()
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        OutlinedTextField(
            value = vm.email,
            onValueChange = { vm.email = it },
            label = { Text("Correo institucional") },
            isError = vm.emailError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        LoginErrorText(vm.emailError)

        OutlinedTextField(
            value = vm.password,
            onValueChange = { vm.password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = vm.passwordError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        LoginErrorText(vm.passwordError)

        // Error general: datos con buen formato pero que no coinciden
        LoginErrorText(vm.loginError)

        Button(
            onClick = {
                if (vm.login()) onLoggedIn()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }
    }
}

@Composable
private fun LoginErrorText(error: String?) {
    AnimatedVisibility(visible = error != null) {
        Text(
            text = error ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}