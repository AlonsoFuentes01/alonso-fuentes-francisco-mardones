package com.example.aplicacion_escuela.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion_escuela.data.UserRepository
import com.example.aplicacion_escuela.data.VistaInspector
import com.example.aplicacion_escuela.screens.AdminHomeScreen
import com.example.aplicacion_escuela.screens.AuthScreen
import com.example.aplicacion_escuela.screens.BandejaInspectorScreen
import com.example.aplicacion_escuela.screens.ConsultarTicketScreen
import com.example.aplicacion_escuela.screens.DocenteHomeScreen
import com.example.aplicacion_escuela.screens.GestionarPerfilScreen
import com.example.aplicacion_escuela.screens.HomeScreen
import com.example.aplicacion_escuela.screens.NuevoTicketScreen
import okhttp3.Credentials

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
                    onAbrirVista = { vista -> nav.navigate("bandeja_inspector/${vista.ruta}") },
                    onLogout = logout,
                    onGestionarPerfil = irAPerfil
                )
            } else {
                DocenteHomeScreen(
                    name = (user?.name ?: "").substringBefore("@"),
                    onNuevoTicket = { nav.navigate("nuevo_ticket") },
                    onConsultarTicket = { nav.navigate("consultar_ticket") },
                    onLogout = logout,
                    onGestionarPerfil = irAPerfil
                )
            }
        }

        composable("nuevo_ticket") {
            val user = repo.getSessionUser()
            if (user == null || user.role == "ADMIN") {
                LaunchedEffect(Unit) { nav.popBackStack() }
            } else {
                NuevoTicketScreen(onVolver = { nav.popBackStack() })
            }
        }

        composable("bandeja_inspector/{vista}") { entrada ->
            val ruta = entrada.arguments?.getString("vista")
            val vista = VistaInspector.entries.firstOrNull { it.ruta == ruta }
            val inspector = repo.getSessionUser()
            if (inspector?.role == "ADMIN" && vista != null) {
                BandejaInspectorScreen(
                    vista = vista,
                    autorizacion = Credentials.basic("inspector", inspector.password),
                    onVolver = { nav.popBackStack() }
                )
            } else {
                LaunchedEffect(Unit) { nav.popBackStack() }
            }
        }

        composable("consultar_ticket") {
            ConsultarTicketScreen(onVolver = { nav.popBackStack() })
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