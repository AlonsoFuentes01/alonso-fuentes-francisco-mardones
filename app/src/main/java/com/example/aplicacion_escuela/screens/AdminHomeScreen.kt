package com.example.aplicacion_escuela.screens

import androidx.compose.runtime.Composable

@Composable
fun AdminHomeScreen(
    name: String,
    onLogout: () -> Unit,
    onHistorialEspacios: () -> Unit = {},
    onSolicitudesAprobadas: () -> Unit = {},
    onSolicitudesPendientes: () -> Unit = {},
    onEspaciosReservados: () -> Unit = {},
    onCuentaAdmin: () -> Unit = {},
    onGestionarPerfil: () -> Unit = {}
) {
    HomeShell(
        name = name,
        cuentaLabel = "Cuenta Admin",
        onCuenta = onCuentaAdmin,
        onGestionarPerfil = onGestionarPerfil,
        onLogout = onLogout
    ) {
        HomeButton("Historial Espacios utilizados", onHistorialEspacios)
        HomeButton("Solicitudes aprobadas", onSolicitudesAprobadas)
        HomeButton("Solicitudes pendientes", onSolicitudesPendientes)
        HomeButton("Espacios Reservados", onEspaciosReservados)
    }
}