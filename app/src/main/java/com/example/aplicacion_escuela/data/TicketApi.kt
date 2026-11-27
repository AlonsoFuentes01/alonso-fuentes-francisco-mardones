package com.example.aplicacion_escuela.data

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class CrearTicketRequest(
    @SerializedName("categoria") val categoria: String,
    @SerializedName("ubicacion") val ubicacion: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fotoBase64") val fotoBase64: String? = null
)

data class TicketCreado(
    @SerializedName("numero") val numero: Long,
    @SerializedName("estado") val estado: String,
    @SerializedName("fotoAdjunta") val fotoAdjunta: Boolean = false
)

interface TicketApi {
    @GET("api/tickets/{numero}")
    suspend fun buscarTicket(@Path("numero") numero: Long): TicketCreado

    @POST("api/tickets")
    suspend fun crearTicket(@Body solicitud: CrearTicketRequest): TicketCreado
}

object TicketApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://mpaz-ticket-service.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api: TicketApi = retrofit.create(TicketApi::class.java)
    val inspector: InspectorApi = retrofit.create(InspectorApi::class.java)
}