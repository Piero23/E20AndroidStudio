package com.example.e20frontendmobile.model

import com.example.e20frontendmobile.JavaLocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class UserProfile(
    val id: String = "",
    val username: String = "",

    val email: String = "",
    @Serializable(with = JavaLocalDateSerializer::class)

    @SerialName("dataNascita") val birthDate: LocalDate? = null,
)