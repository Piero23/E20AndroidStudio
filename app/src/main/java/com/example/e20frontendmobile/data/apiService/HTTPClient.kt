package com.example.e20frontendmobile.data.apiService

import android.content.Context
import com.example.e20frontendmobile.data.auth.AuthManager
import com.example.e20frontendmobile.data.auth.AuthStateStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking

val myHttpClient = HttpClient(Android) {
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
    install(ContentNegotiation) {
        json()
    }
}

fun getToken(context: Context): String? = runBlocking {
    val storage = AuthStateStorage(context)
    val authManager = AuthManager(context)

    val job = CompletableDeferred<String?>()

    authManager.getValidAccessToken(
        storage,
        onSuccess = { freshToken ->
            job.complete(freshToken)
        },
        onError = { ex ->
            println("Errore nel refresh token: $ex")
            job.complete(null)
        }
    )

    val token = job.await()

    if (token == null) {
        println("Nessun token disponibile, serve login")
    }

    return@runBlocking token
}
