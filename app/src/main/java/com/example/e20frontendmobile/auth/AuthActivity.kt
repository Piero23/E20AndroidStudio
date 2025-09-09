package com.example.e20frontendmobile.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import net.openid.appauth.AuthState
import net.openid.appauth.TokenResponse

class AuthActivity : ComponentActivity() {

    private lateinit var authManager: AuthManager
    private var authState: AuthState? = null

    private val RC_AUTH = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authManager = AuthManager(this)
        authManager.startLogin(this@AuthActivity, RC_AUTH)

    }

    @Deprecated("Use ActivityResult API, kept simple for demo")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (resultCode == RESULT_OK || data != null) {
            authManager.handleAuthResponse(
                requestCode,
                resultCode,
                data,
                RC_AUTH,
                onSuccess = { tokenResponse: TokenResponse, state: AuthState ->

                    val token = tokenResponse.accessToken
                    println("Access Token completo: $token")
                    authState = state

                    // ✅ Salvataggio locale
                    val storage = AuthStateStorage(this)
                    storage.writeAuthState(state)

                    finish()
                },
                onError = { ex ->
                    println("Errore autenticazione: ${ex?.errorDescription}")
                    finish()
                }
            )
        }

        authState?.lastTokenResponse?.let {
            println("Access Token: ${it.accessToken?.take(10)}...")
        }

        finish()
    }
}