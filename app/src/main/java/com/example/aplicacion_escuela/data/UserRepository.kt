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

    fun register(user: User): Boolean {
        val users = getUsers()
        if (users.any { it.email.equals(user.email, ignoreCase = true) }) return false
        val json = JSONArray()
        (users + user).forEach {
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
        return true
    }
    fun login(email: String, password: String): User? =
        getUsers().find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }

    fun setSession(email: String?) {
        prefs.edit().putString("session", email).apply()
    }

    fun getSession(): String? = prefs.getString("session", null)


}