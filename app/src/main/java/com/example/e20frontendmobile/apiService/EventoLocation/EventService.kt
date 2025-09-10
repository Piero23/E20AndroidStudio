package com.example.e20frontendmobile.apiService.EventoLocation

import android.content.Context
import com.example.e20frontendmobile.apiService.getToken
import com.example.e20frontendmobile.apiService.myHttpClient
import com.example.e20frontendmobile.model.Event
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

class EventService(private val context: Context) {
    var ip = ""

    // 🔹 GET Event by ID
    fun findById(idEvento: Long): Event? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/evento/$idEvento") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findById: ${e.message}")
            null
        }
    }

    // 🔹 GET all events
    fun findAll(): List<Event>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/evento/") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findAll: ${e.message}")
            null
        }
    }

    // 🔹 GET image (ritorna bytes come Base64 string)
    fun getImage(id: Long): String? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/evento/$id/image") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore getImage: ${e.message}")
            null
        }
    }

    // 🔹 POST create event
    fun create(event: Event): Event? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.post("http://$ip:8060/api/evento") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(event)
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore create event: ${e.message}")
            null
        }
    }

    // 🔹 PUT edit event
    fun edit(id: Long, event: Event): Event? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.put("http://$ip:8060/api/evento/$id") {
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
            val response: HttpResponse = myHttpClient.delete("http://$ip:8060/api/evento/$id") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking response.status.value in 200..299
        } catch (e: Exception) {
            println("Errore delete event: ${e.message}")
            false
        }
    }

    // 🔹 GET remaining spots
    fun spotsLeft(id: Long): Int? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/evento/$id/spots") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore spotsLeft: ${e.message}")
            null
        }
    }

    // 🔹 GET bookings
    fun getBookings(id: Long): List<Any>? = runBlocking { // qui puoi creare una data class Ticket se vuoi
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/evento/$id/bookings") {
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
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/evento/myEvents") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore getFromManager: ${e.message}")
            null
        }
    }
}
