package com.example.aplicacion_escuela.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class User(
    val name: String,
    val email: String,
    val password: String,
    val docPath: String,
    val role: String = "DOCENTE"
)

class UserRepository(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    init {
        // Crea la cuenta de administrador ficticia si todavía no existe
        register(
            User(
                name = "Inspectoría",
                email = "admin@admin.escuela.cl",
                password = "admin123",
                docPath = "",
                role = "ADMIN"
            )
        )
    }
    fun getUsers(): List<User> {
        val json = JSONArray(prefs.getString("users", "[]"))
        return (0 until json.length()).map {
            val o = json.getJSONObject(it)
            User(
                o.getString("name"),
                o.getString("email"),
                o.getString("password"),
                o.getString("docPath"),
                o.optString("role", "DOCENTE")
            )
        }
    }

    private fun saveUsers(users: List<User>) {
        val json = JSONArray()
        users.forEach {
            json.put(
                JSONObject()
                    .put("name", it.name)
                    .put("email", it.email)
                    .put("password", it.password)
                    .put("docPath", it.docPath)
                    .put("role", it.role)
            )
        }
        prefs.edit().putString("users", json.toString()).apply()
    }

    fun register(user: User): Boolean {
        val users = getUsers()
        if (users.any { it.email.equals(user.email, ignoreCase = true) }) return false
        saveUsers(users + user)
        return true
    }
    fun login(email: String, password: String): User? =
        getUsers().find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }

    /** Cambia la contraseña. Retorna false si la contraseña actual no coincide. */
    fun changePassword(email: String, currentPassword: String, newPassword: String): Boolean {
        val users = getUsers()
        val user = users.find { it.email.equals(email, ignoreCase = true) } ?: return false
        if (user.password != currentPassword) return false
        saveUsers(users.map { if (it === user) it.copy(password = newPassword) else it })
        return true
    }

    fun setSession(email: String?) {
        prefs.edit().putString("session", email).apply()
    }

    fun getSession(): String? = prefs.getString("session", null)

    /** Usuario completo de la sesión activa (para saber su rol y nombre) */
    fun getSessionUser(): User? {
        val email = getSession() ?: return null
        return getUsers().find { it.email.equals(email, ignoreCase = true) }
    }
}