package com.example.e20frontendmobile.apiService

import android.content.Context
import com.example.e20frontendmobile.data.apiService.ApiParent
import com.example.e20frontendmobile.data.apiService.getToken
import com.example.e20frontendmobile.data.apiService.myHttpClient
import com.example.e20frontendmobile.model.Event
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import java.util.UUID

class PreferitiService(private val context: Context) : ApiParent() {

    /**
     * Recupera tutti gli eventi preferiti di un utente
     */
    fun getAllPreferiti(utenteId: UUID): List<Event>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/utente/$utenteId/preferiti") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking response.body()
        } catch (e: Exception) {
            println("Errore getAllPreferiti: ${e.message}")
            null
        }
    }

    /**
     * Aggiunge un evento ai preferiti di un utente
     */
    fun aggiungiAiPreferiti(utenteId: UUID, eventoId: Long): Boolean = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.post("http://$ip:8060/api/utente/$utenteId/preferiti") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(mapOf("eventoId" to eventoId))
            }
            return@runBlocking response.status.value in 200..299
        } catch (e: Exception) {
            println("Errore aggiungiAiPreferiti: ${e.message}")
            false
        }
    }

    /**
     * Rimuove un evento dai preferiti di un utente
     */
    fun rimuoviDaiPreferiti(utenteId: UUID, eventoId: Long): Boolean = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.delete("http://$ip:8060/api/utente/$utenteId/preferiti") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(mapOf("eventoId" to eventoId))
            }
            return@runBlocking response.status.value in 200..299
        } catch (e: Exception) {
            println("Errore rimuoviDaiPreferiti: ${e.message}")
            false
        }
    }
}
