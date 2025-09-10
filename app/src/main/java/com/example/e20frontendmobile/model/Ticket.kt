package com.example.e20frontendmobile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    @SerialName("nome") var nome: String = "",
    @SerialName("cognome") var cognome: String = "",
    @SerialName("email") var email: String = "",
    @SerialName("dataNascita") var dataNascita: String = "",
    @SerialName("idEvento") var idEvento: Long = 0L,
    @SerialName("e_valido") var eValido: Boolean = true
)
