package com.example.aplicacion_escuela.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room3.Entity

// Tabla "usuarios". Solo datos ficticios.
@Entity(tableName = "usuarios")
data class Usuario(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val correo: String,

    val clave: String,

    val perfil: Perfil
)