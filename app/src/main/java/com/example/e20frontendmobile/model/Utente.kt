package com.example.e20frontendmobile.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Utente(
    @SerialName("username")
    val username: String,

    @SerialName("email")
    val email: String,

    @SerialName("data_nascita")
    val dataNascita: LocalDateTime

    //TODO seguiti e seguaci vanno qui?
)