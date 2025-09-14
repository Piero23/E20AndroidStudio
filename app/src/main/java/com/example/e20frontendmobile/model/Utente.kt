package com.example.e20frontendmobile.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Utente(

    val id: String,

    val username: String,

    val email: String,

    val dataNascita: LocalDateTime

    //TODO seguiti e seguaci vanno qui?
)