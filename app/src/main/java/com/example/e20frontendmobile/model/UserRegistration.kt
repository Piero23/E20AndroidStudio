package com.example.e20frontendmobile.model

import com.example.e20frontendmobile.JavaLocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate

@Serializable
data class UserRegistration(
    val username: String = "",
    val password: String = "",

    val email: String = "",
    @Serializable(with = JavaLocalDateSerializer::class)

    @SerialName("dataNascita") val birthDate: LocalDate? = null,

    @Transient // Not in the Serial Version
    val errors: Map<String, String> = emptyMap()
)