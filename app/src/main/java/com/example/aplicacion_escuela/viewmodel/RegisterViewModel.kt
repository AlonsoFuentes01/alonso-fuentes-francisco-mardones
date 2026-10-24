package com.example.aplicacion_escuela.viewmodel

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.aplicacion_escuela.data.User
import com.example.aplicacion_escuela.data.UserRepository
import java.io.File

class RegisterViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UserRepository(app)

    // Lo que el usuario escribe
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var fileUri by mutableStateOf<Uri?>(null)
    var fileName by mutableStateOf("")

    // Mensajes de error (null = sin error)
    var nameError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var fileError by mutableStateOf<String?>(null)

    fun onFileSelected(uri: Uri) {
        fileUri = uri
        fileError = null
        getApplication<Application>().contentResolver
            .query(uri, null, null, null, null)?.use { c ->
                if (c.moveToFirst()) {
                    fileName = c.getString(c.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
    }

    private fun validate(): Boolean {
        nameError = if (name.isBlank()) "El nombre es obligatorio" else null
        emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Correo no válido" else null
        passwordError = if (password.length < 6) "Mínimo 6 caracteres" else null
        fileError = if (fileUri == null) "Debes subir el archivo que valida que eres docente" else null
        return listOf(nameError, emailError, passwordError, fileError).all { it == null }
    }

    fun register(): Boolean {
        if (!validate()) return false
        val ctx = getApplication<Application>()

        // Copiamos el archivo a la carpeta privada de la app
        val dir = File(ctx.filesDir, "validacion").apply { mkdirs() }
        val dest = File(dir, "${System.currentTimeMillis()}_$fileName")
        ctx.contentResolver.openInputStream(fileUri!!)?.use { input ->
            dest.outputStream().use { input.copyTo(it) }
        }

        val ok = repo.register(User(name.trim(), email.trim(), password, dest.absolutePath))
        if (!ok) {
            dest.delete()
            emailError = "Este correo ya está registrado"
        }
        return ok
    }
}