package com.example.aplicacion_escuela.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class User(
    val name: String,
    val email: String,
    val password: String,
    val docPath: String
)

class UserRepository(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun getUsers(): List<User> {
        val json = JSONArray(prefs.getString("users", "[]"))
        return (0 until json.length()).map {
            val o = json.getJSONObject(it)
            User(o.getString("name"), o.getString("email"), o.getString("password"), o.getString("docPath"))
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
            )
        }
        prefs.edit().putString("users", json.toString()).apply()
        return true
    }
}