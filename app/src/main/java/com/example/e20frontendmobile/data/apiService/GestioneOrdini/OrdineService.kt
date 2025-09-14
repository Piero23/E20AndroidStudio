package com.example.e20frontendmobile.apiService

import android.content.Context
import com.example.e20frontendmobile.data.apiService.ApiParent
import com.example.e20frontendmobile.data.apiService.getToken
import com.example.e20frontendmobile.data.apiService.myHttpClient
import com.example.e20frontendmobile.model.Ordine
import com.example.e20frontendmobile.model.Ticket
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking
import java.util.UUID

class OrdineService(private val context: Context) : ApiParent()  {



    // 🔹 GET all orders
    fun findAll(): List<Ordine>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/ordine") {
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
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/ordine/$id") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findById: ${e.message}")
            null
        }
    }

    // 🔹 GET all orders by user

    suspend fun findAllByUtente(utenteId: String?): List<Ordine>? {
        val token = getToken(context)
        return try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/ordine/utente") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("utente", utenteId.toString())
            }

            return if (response.status.value in 200..299) {
                val ordini: List<Ordine> = response.body()
                ordini
            } else null
        } catch (e: Exception) {
            println("Errore findAllByUtente: ${e.message}")
            null
        }
    }

    // 🔹 GET all tickets by ordine
    suspend fun findAllBigliettiByOrdine(ordineId: UUID): List<Ticket>?  {
        val token = getToken(context)
        return try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/ordine/biglietti") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("ordine", ordineId.toString())
            }
            return if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findAllBigliettiByOrdine: ${e.message}")
            null
        }
    }
}
