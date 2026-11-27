package com.example.aplicacion_escuela.data

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class ClimaRespuesta(
    @SerializedName("current") val actual: ClimaActual?
)

data class ClimaActual(
    @SerializedName("temperature_2m") val temperatura: Double?,
    @SerializedName("wind_speed_10m") val velocidadViento: Double?
)

interface ClimaApi {
    @GET("v1/forecast?latitude=-33.45&longitude=-70.67&current=temperature_2m,wind_speed_10m")
    suspend fun consultarSantiago(): ClimaRespuesta
}

object ClimaApiClient {
    val api: ClimaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClimaApi::class.java)
    }
}
