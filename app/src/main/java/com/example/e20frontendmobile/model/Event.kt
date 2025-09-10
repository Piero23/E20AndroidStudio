package com.example.e20frontendmobile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class Event(
    val id: String,
    @SerialName("descrizione") val description: String,
    @SerialName("nome") val title: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("data") val date: LocalDateTime,
    @SerialName("location") val location: Location,
    @SerialName("immagine") val image: String,
    @SerialName("posti") val posti: Int,
    @SerialName("prezzo") val prezzo: Int,
    @SerialName("age_restricted") val restricted: Boolean
)
