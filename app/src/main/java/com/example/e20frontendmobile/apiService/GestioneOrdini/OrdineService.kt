package com.example.e20frontendmobile.apiService

import android.content.Context
import com.example.e20frontendmobile.model.Ordine
import com.example.e20frontendmobile.model.Ticket
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import java.util.UUID

class OrdineService(private val context: Context) {
    var ip = "192.168.1.14"

    // 🔹 GET all orders
    fun findAll(): List<Ordine>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/ordine") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findAll: ${e.message}")
            null
        }
    }

    // 🔹 GET ordine by ID
    fun findById(id: UUID): Ordine? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/ordine/$id") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findById: ${e.message}")
            null
        }
    }

    // 🔹 GET all orders by user
    fun findAllByUtente(utenteId: UUID): List<Ordine>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/ordine/utente") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("utente", utenteId.toString())
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findAllByUtente: ${e.message}")
            null
        }
    }

    // 🔹 GET all tickets by ordine
    fun findAllBigliettiByOrdine(ordineId: UUID): List<Ticket>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/ordine/biglietti") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("ordine", ordineId.toString())
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findAllBigliettiByOrdine: ${e.message}")
            null
        }
    }
}
