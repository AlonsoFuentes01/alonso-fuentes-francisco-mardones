package com.example.aplicacion_escuela.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion_escuela.data.UserRepository
import com.example.aplicacion_escuela.screens.AdminHomeScreen
import com.example.aplicacion_escuela.screens.AuthScreen
import com.example.aplicacion_escuela.screens.DocenteHomeScreen
import com.example.aplicacion_escuela.screens.GestionarPerfilScreen
import com.example.aplicacion_escuela.screens.HomeScreen

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val repo = remember { UserRepository(context) }
    val nav = rememberNavController()

    val start = if (repo.getSession() != null) "welcome" else "auth"

    NavHost(navController = nav, startDestination = start) {

        composable("auth") {
            AuthScreen(
                onLoggedIn = {
                    nav.navigate("welcome") { popUpTo("auth") { inclusive = true } }
                }
            )
        }

        composable("welcome") {
            HomeScreen(
                email = repo.getSession() ?: "",
                onContinue = {
                    nav.navigate("home") { popUpTo("welcome") { inclusive = true } }
                }
            )
        }

        composable("home") {
            val user = repo.getSessionUser()
            val logout = {
                repo.setSession(null)
                nav.navigate("auth") { popUpTo("home") { inclusive = true } }
            }
            val irAPerfil = { nav.navigate("perfil") }

            if (user?.role == "ADMIN") {
                AdminHomeScreen(
                    name = user.name,
                    onLogout = logout,
                    onGestionarPerfil = irAPerfil
                )
            } else {
                DocenteHomeScreen(
                    name = (user?.name ?: "").substringBefore("@"),
                    onLogout = logout,
                    onGestionarPerfil = irAPerfil
                )
            }
        }

        composable("perfil") {
            GestionarPerfilScreen(
                onChangePassword = { actual, nueva ->
                    val email = repo.getSession() ?: ""
                    repo.changePassword(email, actual, nueva)
                },
                onBack = { nav.popBackStack() }
            )
        }
    }
}