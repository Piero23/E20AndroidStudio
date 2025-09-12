package com.example.e20frontendmobile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    var id : String? = null,
    @SerialName("nome") var nome: String? = null,
    @SerialName("cognome") var cognome: String? = null,
    @SerialName("email") var email: String? = null,
    var data_nascita: String? = null,
    @SerialName("idEvento") var idEvento: Long? = null,
    @SerialName("e_valido") var eValido: Boolean? = null
)
