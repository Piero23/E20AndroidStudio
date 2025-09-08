package com.example.e20frontendmobile

import com.example.e20frontendmobile.model.Event
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun getFun(): String? = runBlocking {
    try {
        //TODO fare in modo che da loggati non scoppi nulla
        val response: HttpResponse = myHttpClient.get("http://localhost:8060/api/utente/test")
        return@runBlocking if (response.status.value in 200..299) {
            //response.body<Event>()
            response.bodyAsText()
        } else {
            println("Errore server: ${response.status.value}")
            null
        }
    } catch (e: Exception) {
        println("Errore nella GET: ${e.message}")
        null
    }
}


fun postFun() = runBlocking {
    launch{
        val post: HttpResponse = myHttpClient.post("https://www.domain.com/path/file.html") {
            setBody("body content goes here")
        }
        when (post.status.value) {
            in 200..299 -> {
                val evento = post.body() as Event
            }
            else -> {
                // Handle various server errors
            }
        }
    }
}



