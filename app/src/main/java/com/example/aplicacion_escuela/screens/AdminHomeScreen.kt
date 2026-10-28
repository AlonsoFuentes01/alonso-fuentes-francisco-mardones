package com.example.aplicacion_escuela.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores tomados de la maqueta
private val HeaderBlue = Color(0xFF4A6CFF)
private val FooterBlue = Color(0xFF4A5FBF)
private val BodyGray = Color(0xFFE8E8E8)

@Composable
fun AdminHomeScreen(
    name: String,
    onLogout: () -> Unit,
    onHistorialEspacios: () -> Unit = {},
    onSolicitudesAprobadas: () -> Unit = {},
    onSolicitudesPendientes: () -> Unit = {},
    onEspaciosReservados: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // ── Header: menú + saludo ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(HeaderBlue)
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            var menuOpen by remember { mutableStateOf(false) }

            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                IconButton(onClick = { menuOpen = true }) {
                    HamburgerIcon()
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        onClick = {
                            menuOpen = false
                            onLogout()
                        }
                    )
                }
            }

            Text(
                text = "Hola, $name",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            )
        }

        // ── Cuerpo: 4 botones centrados ──
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(BodyGray)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AdminButton("Historial Espacios utilizados", onHistorialEspacios)
            AdminButton("Solicitudes aprobadas", onSolicitudesAprobadas)
            AdminButton("Solicitudes pendientes", onSolicitudesPendientes)
            AdminButton("Espacios Reservados", onEspaciosReservados)
        }

        // ── Barra inferior ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(FooterBlue)
        )
    }
}

/** Ícono de menú dibujado a mano (3 barras) para no depender de material-icons */
@Composable
private fun HamburgerIcon() {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(width = 22.dp, height = 2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(Color.White)
            )
        }
    }
}

@Composable
private fun AdminButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color.DarkGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}