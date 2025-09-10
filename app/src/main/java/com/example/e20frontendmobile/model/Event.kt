package com.example.e20frontendmobile.model

import android.graphics.Bitmap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class Event(
    val id: Long,
    @SerialName("descrizione") val description: String,
    @SerialName("nome") val title: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("data") val date: LocalDateTime,
    @SerialName("locationId") val locationId: Long,
    @SerialName("immagine") val image: String? = null,
    @SerialName("posti") val posti: Int,
    @SerialName("prezzo") val prezzo: Double,
    @SerialName("age_restricted") val restricted: Boolean,
    val organizzatore: String,
    val b_riutilizzabile: Boolean,
    val b_nominativo: Boolean
)
