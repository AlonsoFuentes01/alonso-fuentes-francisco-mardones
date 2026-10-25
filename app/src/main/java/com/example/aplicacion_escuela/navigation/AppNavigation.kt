package com.example.aplicacion_escuela.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion_escuela.data.UserRepository
import com.example.aplicacion_escuela.screens.AuthScreen
import com.example.aplicacion_escuela.screens.HomeScreen

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val repo = remember { UserRepository(context) }
    val nav = rememberNavController()

    // Si ya hay una sesión guardada, parte directo en la bienvenida
    val start = if (repo.getSession() != null) "home" else "auth"

    NavHost(navController = nav, startDestination = start) {
        composable("auth") {
            AuthScreen(
                onLoggedIn = {
                    nav.navigate("home") { popUpTo("auth") { inclusive = true } }
                }
            )
        }
        composable("home") {
            HomeScreen(
                email = repo.getSession() ?: "",
                onLogout = {
                    repo.setSession(null)
                    nav.navigate("auth") { popUpTo("home") { inclusive = true } }
                }
            )
        }
    }
}