package com.example.e20frontendmobile.data.apiService.Utente


import android.content.Context
import android.util.Log
import com.example.e20frontendmobile.data.apiService.ApiParent
import com.example.e20frontendmobile.data.apiService.getToken
import com.example.e20frontendmobile.data.apiService.myHttpClient
import com.example.e20frontendmobile.model.Utente
import com.example.e20frontendmobile.model.UserRegistration
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
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



    /// Suspend Functions

    // Get Current Logged User
    suspend fun getLoggedUser(): Utente? = withContext(Dispatchers.IO) {
        val token = getToken(context) ?: return@withContext null
        try {
            val response = myHttpClient.get("https://$ip:8060/api/utente/me") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (response.status.value in 200..299) {
                Log.d("UtenteService", "Loading logged user: ${response.bodyAsText()}")
                val jsonString = response.bodyAsText()

                val user = Json.decodeFromString(Utente.serializer(), jsonString)
                //Log.d("UtenteService", "Loading unserialized user: ${user}")
                user

            }
            else null

        }
        catch (e: Exception) {
            Log.d("UtenteService", "Errore getLoggedUser: ${e.message}")
            null
        }

    }

    // Get  User
    suspend fun getUtente(username: String): Utente? = withContext(Dispatchers.IO) {
        val token = getToken(context) ?: return@withContext null
        try {
            val response = myHttpClient.get("https://$ip:8060/api/utente/$username") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (response.status.value in 200..299) {
                Log.d("UtenteService", "Loading common user: ${response.bodyAsText()}")
                val jsonString = response.bodyAsText()

                val user = Json.decodeFromString(Utente.serializer(), jsonString)
                Log.d("UtenteService", "Loading unserialized common user: ${user}")
                user

            }
            else null

        }
        catch (e: Exception) {
            Log.d("UtenteService", "Errore getUtente: ${e.message}")
            null
        }

    }

    // Get Seguaci (Followers) of a Given User
    suspend fun getSeguaci(username: String): List<Utente> = withContext(Dispatchers.IO) {
        try {
            val token = getToken(context)
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/utente/$username/seguaci") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (response.status.value in 200..299) {
                Log.d("UtenteService", "Loading Seguaci of $username: ${response.bodyAsText()}")
                val jsonString = response.bodyAsText()

                val users = Json.decodeFromString(ListSerializer(Utente.serializer()), jsonString)
                //Log.d("UtenteService", "Loading unserialized Seguaci of $username: ${users}")
                users

            }
            else {
                println("Errore getSeguaci: ${response.status.value}")
                emptyList()
            }
        }
        catch (e: Exception) {
            Log.d("UtenteService", "Errore getSeguaci: ${e.message}")
            emptyList()
        }
    }

    // Get Seguiti (Following) of a Given User
    suspend fun getSeguiti(username: String): List<Utente> = withContext(Dispatchers.IO) {
        try {
            val token = getToken(context)
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/utente/$username/seguiti") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (response.status.value in 200..299) {

                Log.d("UtenteService", "Loading Seguiti of $username: ${response.bodyAsText()}")
                val jsonString = response.bodyAsText()

                val users = Json.decodeFromString(ListSerializer(Utente.serializer()), jsonString)
                //Log.d("UtenteService", "Loading unserialized Seguiti of $username: ${users}")
                users

            }
            else {
                println("Errore getSeguiti: ${response.status.value}")
                emptyList()
            }
        }
        catch (e: Exception) {
            Log.d("UtenteService", "Errore getSeguiti: ${e.message}")
            emptyList()
        }
    }

    // Get Seguaci & Seguiti of the Logged User
    suspend fun getSeguaciOfLoggedUser(): List<Utente> {
        val user = getLoggedUser() ?: return emptyList()
        return getSeguaci(user.username)
    }

    suspend fun getSeguitiOfLoggedUser(): List<Utente> {
        val user = getLoggedUser() ?: return emptyList()
        return getSeguiti(user.username)
    }

    suspend fun isInUtenteSeguiti(utente: Utente): Boolean {
        val seguiti = getSeguitiOfLoggedUser()
        if (seguiti.contains(utente)) {
            return false
        }
        return true;
    }

    suspend fun aggiungiAiSeguiti(username: String): Boolean {
        val user = getLoggedUser() ?: return false
        val token = getToken(context)
        return try {
            val response: HttpResponse = myHttpClient.post("https://$ip:8060/api/utente/${user.username}/seguiti") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(mapOf("username" to username))
            }
            if (response.status.value in 200..299) {

                Log.d("UtenteService", "${user.username} ha iniziato a seguire $username")
                true

            }
            else {
                println("Errore aggiunta ai seguiti: ${response.status.value}")
                false
            }
        }
        catch (e: Exception) {
            Log.d("UtenteService", "Errore aggiunta ai seguiti: ${e.message}")
            false
        }
    }

    suspend fun rimuoviDaiSeguiti(username: String): Boolean {
        val user = getLoggedUser() ?: return false
        val token = getToken(context)
        return try {
            val response: HttpResponse = myHttpClient.delete("https://$ip:8060/api/utente/${user.username}/seguiti") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(mapOf("username" to username))
            }
            if (response.status.value in 200..299) {

                Log.d("UtenteService", "${user.username} ha smesso di seguire $username")
                true

            }
            else {
                println("Errore rimozione dai seguiti: ${response.status.value}")
                false
            }
        }
        catch (e: Exception) {
            Log.d("UtenteService", "Errore rimozione di seguiti: ${e.message}")
            false
        }
    }

    // Register New User
    suspend fun register(userRegistration: UserRegistration): Boolean = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = myHttpClient.post("https://$ip:8060/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(userRegistration)
            }

            if (response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK) {

                true
            }
            else {
                println("Errore server: ${response.status.value}")
                false
            }
        }
        catch (e: Exception) {
            println("Errore register: ${e.message}")
            false
        }
    }

}