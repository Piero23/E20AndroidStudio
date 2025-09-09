package com.example.e20frontendmobile

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.e20frontendmobile.model.Event
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.ByteBuffer
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

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

fun getMe(): String? = runBlocking {
    try {
        //TODO fare in modo che da loggati non scoppi nulla
        val response: HttpResponse = myHttpClient.get("http://10.0.2.2:9000/auth/ciao"){
            {
                header(HttpHeaders.Authorization, "Bearer eyJraWQiOiIzMzYwZmFjZS04ZDEzLTRjOTgtYjI5YS1hOWU0YjFkMDJiYTciLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJwYW9sZV9ib25vbGlzIiwiYXVkIjoiYW5kcm9pZC1jbGllbnQiLCJuYmYiOjE3NTc0NDU1NDksInNjb3BlIjoib3BlbmlkIHJvbGVzIiwicm9sZXMiOlsiVVNFUiJdLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJwYW9sZV9ib25vbGlzIiwiZXhwIjoxNzU3NDQ5MTQ5LCJpYXQiOjE3NTc0NDU1NDksImp0aSI6IjA0ZjdmMzNmLWJlODAtNGUyMy05MTc3LWQ3ZmUxOTVjMmE1YyIsInVzZXJuYW1lIjoicGFvbGVfYm9ub2xpcyJ9.JjEvYJZflzErhThJZU2XF2Y-WODL502aN-ZrsBgofYZAlmjQ8Q6uS_SVxSw6iYw5tGOWztETtKKhujV6H3DGW-uUjc3aa7n9aUHf9QuofxgRtVPFy4iFAYhI7wRZk8PNpiKu63HmyFJqkJayOPLbYqHgwcz2D50qjd2ir0psnMGU4LvRrwuDU-KxYRe9u8y_5Pta9b7GvujXwr3wxB3s7rgbNXqt0d-FLufrCdv4VKp_xNfDu3BkXiilhDFyQZXGJygHQNdMH4bdLiYKqXLL5N1jFPRvtg3KKyCbYUttQKZOiKpgBQbcfbsHNypvnH-FM9bZrVqlf87fFymv2EFcAg")
            }
        }
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






