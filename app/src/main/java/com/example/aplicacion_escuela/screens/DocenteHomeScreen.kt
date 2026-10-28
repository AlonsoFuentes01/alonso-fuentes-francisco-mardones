package com.example.aplicacion_escuela.screens

import androidx.compose.runtime.Composable

@Composable
fun DocenteHomeScreen(
    name: String,
    onLogout: () -> Unit,
    onBuscarEspacio: () -> Unit = {},
    onMisSolicitudes: () -> Unit = {},
    onEspaciosReservados: () -> Unit = {},
    onCuentaDocente: () -> Unit = {},
    onGestionarPerfil: () -> Unit = {}
) {
    HomeShell(
        name = name,
        cuentaLabel = "Cuenta Docente",
        onCuenta = onCuentaDocente,
        onGestionarPerfil = onGestionarPerfil,
        onLogout = onLogout
    ) {
        HomeButton("Buscar Espacio", onBuscarEspacio)
        HomeButton("Mis Solicitudes", onMisSolicitudes)
        HomeButton("Espacios Reservados", onEspaciosReservados)
    }
}
