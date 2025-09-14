package com.example.e20frontendmobile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: Long? = null,
    @SerialName("nome") val nome: String? = null,
    @SerialName("descrizione") val descrizione: String? = null,
    @SerialName("chiuso") val chiuso: Boolean = false,
    @SerialName("position") val position: String? = null
)
