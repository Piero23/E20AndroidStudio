package com.example.e20frontendmobile.data.apiService.Utente

import android.content.Context
import androidx.compose.animation.scaleOut
import com.example.e20frontendmobile.data.apiService.ApiParent
import com.example.e20frontendmobile.data.apiService.getToken
import com.example.e20frontendmobile.data.apiService.myHttpClient
import com.example.e20frontendmobile.model.Event
import com.example.e20frontendmobile.model.Ticket
import com.example.e20frontendmobile.model.UserRegistration
import com.example.e20frontendmobile.model.Utente
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class UtenteService (private val context: Context) : ApiParent() {

    fun getUtenteSub(): String? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/utente/me") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) {
                val bodyString = response.bodyAsText()

                val json = org.json.JSONObject(bodyString)
                val sub = json.getString("id")

                sub
            } else null
        } catch (e: Exception) {
            println("Errore getUtente: ${e.message}")
            null
        }
    }

    suspend fun search(query: String): List<Utente> {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/utente/search/$query")
                if (response.status.value in 200..299) {
                    val jsonString = response.bodyAsText()
                    val jsonElement = Json.parseToJsonElement(jsonString).jsonObject
                    val contentJson = jsonElement["content"]?.toString() ?: "[]"
                    //TODO usando LocalDateTime da problemi di serializzazione quindi ora nell'utente la date è una stringa
                    Json.decodeFromString(ListSerializer(Utente.serializer()), contentJson)
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

    fun register(userRegistration: UserRegistration) = runBlocking {

        println("LIIIIII")

        println("LIIIIII"+userRegistration.toString())

        println("LIIIIII")

        try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/auth/register") {
                setBody(userRegistration)
            }
            return@runBlocking if (response.status.value in 200..299) {
                val bodyString = response.bodyAsText()

                val json = org.json.JSONObject(bodyString)
                val sub = json.getString("id")

                sub
            } else null
        } catch (e: Exception) {
            println("Errore getUtente: ${e.message}")
            null
        }
    }
}