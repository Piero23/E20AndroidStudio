package com.example.e20frontendmobile.model

import kotlinx.datetime.LocalDateTime


data class Event(
    val id: String,
    val description: String,
    val title: String,
    val date: LocalDateTime,
    val location: String,
    val image: String,
    val posti: Int,
    val prezzo: Int,
    val restricted: Boolean
)