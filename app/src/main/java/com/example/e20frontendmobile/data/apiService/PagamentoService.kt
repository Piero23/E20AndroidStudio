package com.example.e20frontendmobile.data.apiService

import android.content.Context
import com.example.e20frontendmobile.model.Ticket
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import java.util.UUID

class PagamentoService(private val context: Context) {
    var ip = ""

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
        try {
            val response: HttpResponse = myHttpClient.post("http://$ip:8060/api/stripe/checkout") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "utenteId" to utenteId.toString(),
                        "valuta" to valuta,
                        "biglietti" to biglietti
                    )
                )
            }

            val map: Map<String, String> = response.body()
            return@runBlocking map["url"]
        } catch (e: Exception) {
            println("Errore checkout: ${e.message}")
            null
        }
    }
}
