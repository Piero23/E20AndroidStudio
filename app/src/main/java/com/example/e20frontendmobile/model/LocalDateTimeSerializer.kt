package com.example.e20frontendmobile.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.ExperimentalTime

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    @OptIn(ExperimentalTime::class)
    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val instant = value.toInstant(TimeZone.UTC)
        encoder.encodeString(instant.toString()) // formato ISO string
    }

    @OptIn(ExperimentalTime::class)
    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        val instant = Instant.parse(string)
        return instant.toLocalDateTime(TimeZone.currentSystemDefault())
    }
}
