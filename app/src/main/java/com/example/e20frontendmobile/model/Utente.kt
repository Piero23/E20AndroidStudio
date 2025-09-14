package com.example.e20frontendmobile.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Utente(

    val id: String,

    val username: String,

    val email: String,

    val dataNascita: LocalDate?
)