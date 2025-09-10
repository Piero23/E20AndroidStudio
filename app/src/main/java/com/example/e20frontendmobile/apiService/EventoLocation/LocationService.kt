package com.example.e20frontendmobile.apiService.EventoLocation

import android.content.Context
import com.example.e20frontendmobile.apiService.getToken
import com.example.e20frontendmobile.apiService.myHttpClient
import com.example.e20frontendmobile.model.Location
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

class LocationService(private val context: Context) {
    var ip = "192.168.1.14"

    // 🔹 GET all locations
    fun findAll(): List<Location>? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/location") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findAll: ${e.message}")
            null
        }
    }

    // 🔹 GET location by ID
    fun findById(id: Long): Location? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.get("http://$ip:8060/api/location/$id") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore findById: ${e.message}")
            null
        }
    }

    // 🔹 POST create new location
    fun create(location: Location): Location? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.post("http://$ip:8060/api/location") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(location)
            }
            return@runBlocking if (response.status.value in 200..299) response.body() else null
        } catch (e: Exception) {
            println("Errore create: ${e.message}")
            null
        }
    }

    // 🔹 PUT update location
    fun edit(id: Long, location: Location): Location? = runBlocking {
        val token = getToken(context)
        try {
            val response: HttpResponse = myHttpClient.put("http://$ip:8060/api/location/$id") {
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
            val response: HttpResponse = myHttpClient.delete("http://$ip:8060/api/location/$id") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            return@runBlocking response.status.value in 200..299
        } catch (e: Exception) {
            println("Errore delete: ${e.message}")
            false
        }
    }
}
