package com.example.e20frontendmobile.model

import kotlinx.serialization.Serializable

@Serializable
data class Preferiti(
    val utenteId: String? = null,
    val evento: Event? = null
)
