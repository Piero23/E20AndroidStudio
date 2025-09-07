package com.example.e20frontendmobile.model

import android.graphics.Bitmap
import androidx.room.Entity
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
class Event(
    @SerialName("eventId") val id: String,
    @SerialName("description") val description: String,
    @SerialName("title") val title: String,
    @SerialName("date") val date: LocalDateTime,
    @SerialName("location") var location: String,
    @SerialName("image") val image: String,
    @SerialName("posti") val posti: Int,
    @SerialName("prezzo") val prezzo: Int,
    @SerialName("restricted") val restricted: Boolean) {
}