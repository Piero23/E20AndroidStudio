package com.example.e20frontendmobile

import kotlinx.datetime.number
import kotlinx.serialization.KSerializer
import java.time.LocalDate as JavaLocalDate

import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter


// Date Time Conversion ----------------------------------------------------------------------------

// Kotlinx to Java.time
fun kotlinx.datetime.LocalDate.toJavaLocalDate(): JavaLocalDate {
    return JavaLocalDate.of(year, month.number, day)
}

// Java.time to Kotlinx
fun JavaLocalDate.toKotlinLocalDate(): kotlinx.datetime.LocalDate {
    return kotlinx.datetime.LocalDate(year, monthValue, dayOfMonth)
}

// Java LocalDate Serialization
object JavaLocalDateSerializer : KSerializer<JavaLocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: JavaLocalDate) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): JavaLocalDate {
        return JavaLocalDate.parse(decoder.decodeString(), formatter)
    }
}