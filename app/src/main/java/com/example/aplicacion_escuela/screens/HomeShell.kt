package com.example.aplicacion_escuela.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
private val MenuBlue = Color(0xFF4A5FBF)

private const val DURACION_MENU = 600


@Composable
fun HomeShell(
    name: String,
    cuentaLabel: String,
    onCuenta: () -> Unit,
    onGestionarPerfil: () -> Unit,
    onLogout: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(HeaderBlue)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = { menuOpen = true },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    HamburgerIcon()
                }

                Text(
                    text = "Hola, $name",
                    color = Color.White,
                    fontSize = 20.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(start = 56.dp, end = 16.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(BodyGray)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(FooterBlue)
            )
        }

        AnimatedVisibility(
            visible = menuOpen,
            enter = fadeIn(tween(DURACION_MENU)),
            exit = fadeOut(tween(DURACION_MENU))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { menuOpen = false }
            )
        }

        AnimatedVisibility(
            visible = menuOpen,
            modifier = Modifier.align(Alignment.TopStart),
            enter = expandVertically(
                animationSpec = tween(DURACION_MENU),
                expandFrom = Alignment.Top
            ) + fadeIn(tween(DURACION_MENU)),
            exit = shrinkVertically(
                animationSpec = tween(DURACION_MENU),
                shrinkTowards = Alignment.Top
            ) + fadeOut(tween(DURACION_MENU))
        ) {
            SideMenu(
                name = name,
                cuentaLabel = cuentaLabel,
                onClose = { menuOpen = false },
                onCuenta = onCuenta,
                onGestionarPerfil = onGestionarPerfil,
                onLogout = {
                    menuOpen = false
                    onLogout()
                }
            )
        }
    }
}

@Composable
fun HomeButton(text: String, onClick: () -> Unit) {
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

@Composable
private fun SideMenu(
    name: String,
    cuentaLabel: String,
    onClose: () -> Unit,
    onCuenta: () -> Unit,
    onGestionarPerfil: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.92f)
            .clip(RoundedCornerShape(24.dp))
            .background(MenuBlue)
            .border(2.dp, Color.Black, RoundedCornerShape(24.dp))
            // Consume los toques para que no lleguen al fondo oscuro
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {}
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar()
                Spacer(Modifier.width(12.dp))
                Text(text = name, color = Color.White, fontSize = 18.sp, maxLines = 1)
            }
            Text(
                text = "✕",
                color = Color.Black,
                fontSize = 22.sp,
                modifier = Modifier.clickable(onClick = onClose)
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp),
            thickness = 1.dp,
            color = Color.Black
        )

        Spacer(Modifier.height(16.dp))
        MenuItem(cuentaLabel, onCuenta)
        Spacer(Modifier.height(16.dp))
        MenuItem("Gestionar Perfil", onGestionarPerfil)

        Spacer(Modifier.weight(1f))

        Text(
            text = "Cerrar sesión",
            color = Color(0xFFFF1A1A),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable(onClick = onLogout)
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun MenuItem(text: String, onClick: () -> Unit) {
    Column {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 8.dp)
        )
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
    }
}

@Composable
private fun Avatar() {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF6EC1E4)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFD83A))
            )
            Spacer(Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(width = 28.dp, height = 14.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(Color(0xFFFFD83A))
            )
        }
    }
}

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
