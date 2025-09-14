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

class PreferitiService(private val context: Context) : ApiParent() {

    /**
     * Recupera tutti gli eventi preferiti di un utente
     */
    suspend fun getAllPreferiti(utenteId: String?): List<Event> {
        val token = getToken(context)
        return try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/utente/$utenteId/preferiti") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            response.body()
        } catch (e: Exception) {
            println("Errore getAllPreferiti: ${e.message}")
            listOf()
        }
    }

    /**
     * Aggiunge un evento ai preferiti di un utente
     */
    suspend fun aggiungiAiPreferiti(username: String?, eventoId: Long): Boolean {
        val token = getToken(context)
        return try {
            val response: HttpResponse = myHttpClient.post("https://$ip:8060/api/utente/$username/preferiti") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(mapOf("eventoId" to eventoId))
            }
            println("$username  $eventoId" )
            response.status.value in 200..299
        } catch (e: Exception) {
            println("Errore aggiungiAiPreferiti: ${e.message}")
            false
        }
    }

    /**
     * Rimuove un evento dai preferiti di un utente
     */
    suspend fun rimuoviDaiPreferiti(username: String?, eventoId: Long): Boolean {
        val token = getToken(context)
        return try {
            val response: HttpResponse = myHttpClient.delete("https://$ip:8060/api/utente/$username/preferiti") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(mapOf("eventoId" to eventoId))
            }
            response.status.value in 200..299
        } catch (e: Exception) {
            println("Errore rimuoviDaiPreferiti: ${e.message}")
            false
        }
    }
}
