package com.example.e20frontendmobile.data.apiService

import android.content.Context
import com.example.e20frontendmobile.data.auth.AuthManager
import com.example.e20frontendmobile.data.auth.AuthStateStorage
import io.ktor.client.HttpClient
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import io.ktor.client.plugins.*
import io.ktor.http.*
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ssl.NoopHostnameVerifier
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ssl.SSLConnectionSocketFactory
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ssl.TrustSelfSignedStrategy
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClients
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ssl.SSLContextBuilder
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.Url.Companion.serializer
import io.ktor.serialization.kotlinx.json.json
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

//val myHttpClient = HttpClient(Apache) {

val myHttpClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json()
    }

    engine {
        sslManager = { httpsURLConnection ->
            httpsURLConnection.hostnameVerifier = HostnameVerifier { _, _ -> true }

            val trustManager = object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(trustManager), java.security.SecureRandom())
            httpsURLConnection.sslSocketFactory = sslContext.socketFactory
        }
    }
}


//TODO fix refresh
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
