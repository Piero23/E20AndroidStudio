package com.example.e20frontendmobile.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Address(

    val road: String? = null,
    val village: String? = null,
    val postcode: String? = null,
)