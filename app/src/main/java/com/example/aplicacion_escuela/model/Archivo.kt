package com.example.aplicacion_escuela.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla "archivos": guarda los datos de cada archivo que sube un usuario.
// El archivo en sí NO se copia: solo se guarda su dirección (uri).
@Entity(tableName = "archivos")
data class Archivo(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val usuarioId: Int,

    val nombre: String,

    // Dirección del archivo en el teléfono (content://...).
    val uri: String,

    // Tipo de archivo, por ejemplo "application/pdf".
    val tipo: String,

    // Tamaño en bytes (-1 si el teléfono no lo informa).
    val tamanoBytes: Long,

    val fechaCarga: Long = System.currentTimeMillis()
)