package com.example.e20frontendmobile.apiService

import android.content.Context
import com.example.e20frontendmobile.data.apiService.ApiParent
import com.example.e20frontendmobile.data.apiService.getToken
import com.example.e20frontendmobile.data.apiService.myHttpClient
import com.example.e20frontendmobile.model.Ticket
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking
import java.util.UUID

class TicketService(private val context: Context) : ApiParent() {

    fun getAllBiglietti(): List<Ticket>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/biglietto") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore getAllBiglietti: ${e.message}")
            null
        }
    }

    fun getQrCode(id: UUID): String? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/biglietto/$id/qr") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            val map: Map<String, String> = response.body()
            return@runBlocking map["imageBase64"]
        } catch (e: Exception) {
            println("Errore getQrCode: ${e.message}")
            null
        }
    }

    fun validate(id: String): Boolean = runBlocking {
        val token = getToken(context) ?: return@runBlocking false

        try {
            val response: HttpResponse = myHttpClient.post("https://$ip:8060/api/biglietto/$id") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            return@runBlocking response.status.value in 200..299
        } catch (e: Exception) {
            println("Errore validate: ${e.message}")
            return@runBlocking false
        }
    }

    fun getBigliettoEvento(eventoId: Long): List<Ticket>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/biglietto/evento") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("id", eventoId)
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore getBigliettoEvento: ${e.message}")
            null
        }
    }
}
