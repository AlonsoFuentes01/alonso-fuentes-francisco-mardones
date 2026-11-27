package com.example.aplicacion_escuela.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

enum class VistaInspector(val ruta: String, val titulo: String) {
    PENDIENTES("pendientes", "Tickets pendientes"),
    HISTORIAL("historial", "Historial de tickets"),
    ACEPTADOS("aceptados", "Aceptados")
}

data class TicketRevision(
    @SerializedName("numero") val numero: Long,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("ubicacion") val ubicacion: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("fotoAdjunta") val fotoAdjunta: Boolean
)

interface InspectorApi {
    @GET("api/inspector/tickets/{vista}")
    suspend fun listar(
        @Header("Authorization") autorizacion: String,
        @Path("vista") vista: String
    ): List<TicketRevision>

    @PATCH("api/inspector/tickets/{numero}/aceptar")
    suspend fun aceptar(@Header("Authorization") autorizacion: String, @Path("numero") numero: Long)
}
