package com.example.e20frontendmobile.data.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        Log.d("AuthActivity", "requestCode: $requestCode, resultCode: $resultCode, data: $data")

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


                    //  Return Success + optional data
                    val resultIntent = Intent().apply {
                        putExtra("access_token", tokenResponse.accessToken)
                        putExtra("id_token", tokenResponse.idToken)
                    }

                    //  Return Success to the Caller via Intent
                    Log.d("AuthActivity", "Returning RESULT_OK")
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()

                    finish()
                },
                onError = { ex ->
                    Log.e("AuthActivity", "Errore autenticazione: ${ex?.errorDescription}")
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            )
        }

        authState?.lastTokenResponse?.let {
            Log.w("AuthActivity", "Access Token: ${it.accessToken?.take(10)}...")
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}