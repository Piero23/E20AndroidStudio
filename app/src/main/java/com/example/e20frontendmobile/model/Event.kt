package com.example.e20frontendmobile.model

import android.R
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class Event(
    val id: Long,
    @SerialName("descrizione") val description: String,
    @SerialName("nome") val title: String,
    @SerialName("data") val date: String?,
    @SerialName("locationId") val locationId: Long,
    @SerialName("immagine") val image: String? = null,
    @SerialName("posti") val posti: Int,
    @SerialName("prezzo") val prezzo: Double,
    @SerialName("age_restricted") val restricted: Boolean,
    val organizzatore: String,
    val b_riutilizzabile: Boolean,
    val b_nominativo: Boolean
)


