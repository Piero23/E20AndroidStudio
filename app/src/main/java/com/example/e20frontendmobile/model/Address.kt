package com.example.e20frontendmobile.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Address(
    @SerialName("house_number") val houseNumber: String? = null,
    val road: String? = null,
    val village: String? = null,
    val county: String? = null,
    @SerialName("ISO3166-2-lvl6") val iso3166Lvl6: String? = null,
    val state: String? = null,
    @SerialName("ISO3166-2-lvl4") val iso3166Lvl4: String? = null,
    val postcode: String? = null,
    val country: String? = null,
    @SerialName("country_code") val countryCode: String? = null
)