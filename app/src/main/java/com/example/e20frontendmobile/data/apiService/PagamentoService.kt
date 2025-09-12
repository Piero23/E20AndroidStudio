package com.example.e20frontendmobile.data.apiService

import android.content.Context
import android.util.Log
import com.example.e20frontendmobile.model.Ticket
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import java.util.UUID
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.encodeToJsonElement

class PagamentoService(private val context: Context): ApiParent() {

    /**
     * Esegue il checkout passando direttamente utenteId, valuta e lista di biglietti.
     * Ritorna la URL della sessione Stripe o null in caso di errore.
     */
    fun checkout(
        utenteId: UUID,
        valuta: String,
        biglietti: List<Ticket>
    ): String? = runBlocking {
        val token = getToken(context)
        return@runBlocking try {
            println("cazzopalle $biglietti")
            val body = buildJsonObject {
                put("utenteId", utenteId.toString())
                put("valuta", valuta)
                put("biglietti", Json.encodeToJsonElement(biglietti))
            }

            val response: HttpResponse = myHttpClient.post("https://$ip:8060/api/stripe/checkout") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(body)
            }

            val map: Map<String, String> = response.body()
            map["url"]
        } catch (e: Exception) {
            null
        }
    }
}
