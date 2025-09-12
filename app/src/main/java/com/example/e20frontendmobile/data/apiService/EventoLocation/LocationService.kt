package com.example.e20frontendmobile.data.apiService.EventoLocation

import android.content.Context
import com.example.e20frontendmobile.data.apiService.ApiParent
import com.example.e20frontendmobile.data.apiService.getToken
import com.example.e20frontendmobile.data.apiService.myHttpClient
import com.example.e20frontendmobile.model.Address
import com.example.e20frontendmobile.model.Event
import com.example.e20frontendmobile.model.Location
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class LocationService(private val context: Context) : ApiParent()  {

    // 🔹 GET all locations
    fun findAll(): List<Location>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/location") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findAll: ${e.message}")
            null
        }
    }

    suspend fun findById(id: Long): Location?{
        return try {
            val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/location/$id")
            return if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findById: ${e.message}")
            null
        }
    }

    suspend fun getAddress(position: String): Address?{
        val lat = position.split(",")[0]
        val lon = position.split(",")[1]
        return try {
            val response: HttpResponse = myHttpClient.get("https://nominatim.openstreetmap.org/reverse?lat=$lat&lon=$lon&format=json&addressdetails=1")
            if (response.status.value in 200..299) {
                val res = response.bodyAsText()
                val jsonElement = Json.parseToJsonElement(res).jsonObject
                val addressElement = jsonElement["address"] ?: null
                Json.decodeFromJsonElement(Address.serializer(), addressElement!!)
            } else null
        } catch (e: Exception) {
            println("Errore findById: ${e.message}")
            null
        }
    }

    // 🔹 POST create new location
    fun create(location: Location): Location? = runBlocking{
        val token = getToken(context)
        return@runBlocking try {
            val response: HttpResponse = myHttpClient.post("https://$ip:8060/api/location") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(location)
            }
            if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore create: ${e.message}")
            null
        }
    }

    // 🔹 PUT update location
    fun edit(id: Long, location: Location): Location? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.put("https://$ip:8060/api/location/$id") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(location)
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore edit: ${e.message}")
            null
        }
    }

    // 🔹 DELETE location
    fun delete(id: Long): Boolean = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.delete("https://$ip:8060/api/location/$id") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking response.status.value in 200..299
        } catch (e: Exception) {
            println("Errore delete: ${e.message}")
            false
        }
    }

    suspend fun search(query: String): List<Location> {
        val token = getToken(context)
        return withContext(Dispatchers.IO) { // esegue in background
            try {
                val response: HttpResponse = myHttpClient.get("https://$ip:8060/api/location/search/$query"){
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
                if (response.status.value in 200..299) {
                    val jsonString = response.bodyAsText()
                    val jsonElement = Json.parseToJsonElement(jsonString).jsonObject
                    val contentJson = jsonElement["content"]?.toString() ?: "[]"
                    Json.decodeFromString(ListSerializer(Location.serializer()), contentJson)
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
}
