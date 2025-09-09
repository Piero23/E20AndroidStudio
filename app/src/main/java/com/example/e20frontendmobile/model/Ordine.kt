package com.example.e20frontendmobile.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Ordine(
    val utenteId: UUID,
    val bigliettiComprati: Int,
    val importo: Double,
    val dataPagamento: LocalDateTime,
    val biglietti: Set<Ticket> = emptySet()
)
