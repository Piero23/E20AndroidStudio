package com.example.e20frontendmobile.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.datetime.LocalDateTime
import java.util.UUID

@Serializable
data class Ordine(
    @SerialName("utenteId") val utenteId: String, // UUID come stringa per serializzazione
    @SerialName("bigliettiComprati") val bigliettiComprati: Int,
    @SerialName("importo") val importo: Double,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("dataPagamento") val dataPagamento: LocalDateTime,
    @SerialName("biglietti") val biglietti: Set<Ticket> = emptySet()
)