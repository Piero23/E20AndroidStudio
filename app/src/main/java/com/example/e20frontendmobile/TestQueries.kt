package com.example.e20frontendmobile

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.e20frontendmobile.model.Event
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.ByteBuffer

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

fun canemartello(): ImageBitmap? = runBlocking {
    try {
        val response: HttpResponse = myHttpClient.get(
            "https://hips.hearstapps.com/hmg-prod/images/view-of-french-bulldog-standing-on-grass-royalty-free-image-1686889382.jpg?crop=0.716xw:1.00xh;0.199xw,0"
        )

        if (response.status.value in 200..299) {
            val bytes: ByteArray = response.body()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            bitmap?.asImageBitmap()
        } else {
            println("Errore server: ${response.status.value}")
            null
        }
    } catch (e: Exception) {
        println("Errore nella GET: ${e.message}")
        null
    } finally {
        myHttpClient.close()
    }
}






