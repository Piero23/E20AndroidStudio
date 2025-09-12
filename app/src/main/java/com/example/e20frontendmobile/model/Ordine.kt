package com.example.e20frontendmobile.model

import android.R
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.datetime.LocalDateTime
import java.util.UUID

@Serializable
data class Ordine(
    val id : String,
    @SerialName("utenteId") val utenteId: String, // UUID come stringa per serializzazione
    @SerialName("biglietti_comprati") val bigliettiComprati: Int,
    @SerialName("importo") val importo: Double,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("data_pagamento") val dataPagamento: LocalDateTime,
    @SerialName("biglietti") val biglietti: Set<Ticket> = emptySet()
)