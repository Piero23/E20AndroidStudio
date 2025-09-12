package com.example.e20frontendmobile.data.apiService.EventoLocation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.e20frontendmobile.data.apiService.ApiParent
import com.example.e20frontendmobile.data.apiService.getToken
import com.example.e20frontendmobile.data.apiService.myHttpClient
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
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.io.File
import kotlin.math.log

class EventService(private val context: Context) : ApiParent() {

    // 🔹 GET Event by ID
    fun findById(idEvento: Long): Event? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento/$idEvento") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findById: ${e.message}")
            null
        }
    }

    // 🔹 GET all events
    fun findAll(): List<Event> = runBlocking {
        try {
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
        val token = getToken(context)
        return try {
            withContext(Dispatchers.IO) {
                val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento/$id/image") {
                    if (token != null) header(HttpHeaders.Authorization, "Bearer $token")
                }

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

    fun edit(id: Long, event: Event): Event? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.put("https://$ip:8060/api/evento/$id") {
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

    // 🔹 GET my events (by manager)
    fun getFromManager(): List<Event>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/evento/myEvents") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore getFromManager: ${e.message}")
            null
        }
    }

    suspend fun search(query: String): List<Event> {
        return withContext(Dispatchers.IO) { // esegue in background
            try {
                println("Bagio ha sei fuori dal codice")
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
