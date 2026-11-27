package com.example.aplicacion_escuela.screens

import androidx.compose.runtime.Composable
import com.example.aplicacion_escuela.data.VistaInspector

@Composable
fun AdminHomeScreen(
    name: String,
    onLogout: () -> Unit,
    onAbrirVista: (VistaInspector) -> Unit,
    onGestionarPerfil: () -> Unit
) {
    HomeShell(
        name = name,
        cuentaLabel = "Cuenta Inspector",
        onCuenta = onGestionarPerfil,
        onGestionarPerfil = onGestionarPerfil,
        onLogout = onLogout
    ) {
        VistaInspector.entries.forEach { vista ->
            HomeButton(vista.titulo) { onAbrirVista(vista) }
        }
    }
}
