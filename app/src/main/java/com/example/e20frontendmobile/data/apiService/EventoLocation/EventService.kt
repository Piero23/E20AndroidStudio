package com.example.e20frontendmobile.data.apiService.EventoLocation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.e20frontendmobile.data.apiService.ApiParent
import com.example.e20frontendmobile.data.apiService.getToken
import com.example.e20frontendmobile.data.apiService.myHttpClient
import com.example.e20frontendmobile.model.Address
import com.example.e20frontendmobile.model.Event
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.io.File
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.log
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class EventService(private val context: Context) : ApiParent() {

    // 🔹 GET Event by ID
    suspend fun findById(idEvento: Long): Event? {

        return try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento/$idEvento")

            if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findById: ${e.message}")
            null
        }
    }

    suspend fun findName(idEvento: Long): String? {

        return try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento/$idEvento")

            if (response.status.value in 200..299) {
                val res = response.bodyAsText()
//                val jsonElement = Json.parseToJsonElement(res).jsonObject
//                val addressElement = jsonElement["nome"]?.jsonObject

                Log.d("EventService",res)
                res.split(":")[2].split(",")[0].replace("\"", "")
            } else {
                Log.d("EventService","Errore nella funzione "+ response.bodyAsText())
                null
            }

        } catch (e: Exception) {
            println("Errore findById: ${e.message}")
            null
        }
    }

    // GET all events
    suspend fun findAll(): List<Event> {
        return try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento")
            if (response.status.value in 200..299) {
                val jsonString = response.bodyAsText()
                val jsonElement = Json.parseToJsonElement(jsonString).jsonObject
                val contentJson = jsonElement["content"]?.toString() ?: "[]"
                Json.decodeFromString(ListSerializer(Event.serializer()), contentJson)
            } else {
                println("Errore server: ${response.status.value}")
                emptyList()
            }
        } catch (e: Exception) {
            println("Errore findAll: ${e.message}")
            listOf()
        }
    }

    suspend fun getImage(id: Long): Bitmap? {
        return try {
            withContext(Dispatchers.IO) {
                val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento/$id/image")

                if (response.status.value in 200..299) {
                    val bytes = response.body<ByteArray>()
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                } else {
                    println("Errore server: ${response.status.value}")
                    null
                }
            }
        } catch (e: Exception) {
            println("Errore getImageBitmap: ${e.message}")
            null
        }
    }

    suspend fun uploadImageEvento(
        idEvento: Long,
        file: File
    ): Boolean {
        val token = getToken(context)

        return try {
            val response: HttpResponse = myHttpClient.submitFormWithBinaryData(
                url = "https://$ip:8060/api/evento/$idEvento/image",
                formData = formData {
                    append(
                        key = "immagine",
                        value = file.readBytes(),
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg") // o image/png a seconda
                            append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                        }
                    )
                }
            ) {
                method = HttpMethod.Put
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            response.status.isSuccess()
        } catch (e: Exception) {
            println("Errore upload: ${e.message}")
            false
        }
    }

    // 🔹 POST create event
    suspend fun create(event: Event): Event? {

        Log.d("EventCreate",event.toString())

        val token = getToken(context)
        return try {
            val response: HttpResponse = myHttpClient.post("https://$ip:8060/api/evento") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(event)
            }


            return if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore create event: ${e.message}")
            null
        }
    }

    fun edit(event: Event): Event? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.put("https://$ip:8060/api/evento/${event.id}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(event)
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore edit event: ${e.message}")
            null
        }
    }

    // 🔹 DELETE event
    fun delete(id: Long): Boolean = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.delete("https://$ip:8060/api/evento/$id") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking response.status.value in 200..299
        } catch (e: Exception) {
            println("Errore delete event: ${e.message}")
            false
        }
    }


    suspend fun spotsLeft(id: Long): Int {
        return try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento/$id/spots")
            val body = response.bodyAsText()
            println("BODY spotsLeft: $body, status: ${response.status.value}")
            if (response.status.value in 200..299) {
                response.body<String>().toIntOrNull() ?: 0
            } else {
                0
            }
        } catch (e: Exception) {
            println("Errore spotsLeft: ${e.message}")
            0
        }
    }

    // 🔹 GET bookings
    fun getBookings(id: Long): List<Any>? = runBlocking { // qui puoi creare una data class Ticket se vuoi
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento/$id/bookings") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore getBookings: ${e.message}")
            null
        }
    }


    @OptIn(ExperimentalTime::class)
    suspend fun getFromManager(): List<Event>{
        val token = getToken(context)
        return try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento/myEvents") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            if (response.status.value in 200..299) {
                val json = response.bodyAsText()
                val jsonArray = Json.parseToJsonElement(json).jsonArray

                jsonArray.map { elem ->
                    val obj = elem.jsonObject
                    val locationId = obj["location"]?.jsonObject?.get("id")?.jsonPrimitive?.long

                    val rawDate = obj["data"]!!.jsonPrimitive.content
                    val instant = Instant.parse(rawDate)
                    val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

                    Event(
                        id = obj["id"]!!.jsonPrimitive.long,
                        title = obj["nome"]!!.jsonPrimitive.content,
                        description = obj["descrizione"]!!.jsonPrimitive.content,
                        organizzatore = obj["organizzatore"]!!.jsonPrimitive.content,
                        posti = obj["posti"]!!.jsonPrimitive.int,
                        b_riutilizzabile = obj["b_riutilizzabile"]!!.jsonPrimitive.boolean,
                        b_nominativo = obj["b_nominativo"]!!.jsonPrimitive.boolean,
                        restricted = obj["age_restricted"]!!.jsonPrimitive.boolean,
                        locationId = locationId ?: -1,
                        date = localDateTime,
                        prezzo = obj["prezzo"]!!.jsonPrimitive.double
                    )
                }
            } else listOf()
        } catch (e: Exception) {
            println("Errore getFromManager: ${e.message}")
            listOf()
        }
    }

    suspend fun search(query: String): List<Event> {
        return withContext(Dispatchers.IO) { // esegue in background
            try {
                val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento/search/$query")
                if (response.status.value in 200..299) {
                    val jsonString = response.bodyAsText()
                    val jsonElement = Json.parseToJsonElement(jsonString).jsonObject
                    val contentJson = jsonElement["content"]?.toString() ?: "[]"
                    Json.decodeFromString(ListSerializer(Event.serializer()), contentJson)
                } else {
                    println("Errore server: ${response.status.value}")
                    emptyList()
                }
            } catch (e: Exception) {
                println("Errore nella GET: ${e.message}")
                emptyList()
            }

        }
    }


}
