package com.example.aplicacion_escuela.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.aplicacion_escuela.data.UserRepository

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UserRepository(app)

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var loginError by mutableStateOf<String?>(null)

    private fun validate(): Boolean {
        emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Correo no válido" else null
        passwordError = if (password.isEmpty()) "Ingresa tu contraseña" else null
        return emailError == null && passwordError == null
    }

    fun login(): Boolean {
        loginError = null
        if (!validate()) return false

        val user = repo.login(email.trim(), password)
        if (user == null) {
            loginError = "Correo o contraseña incorrectos"
            return false
        }
        repo.setSession(user.email)
        return true
    }
}