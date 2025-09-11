package com.example.e20frontendmobile.data.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import net.openid.appauth.*


/**
 * Gestisce il flusso di autenticazione con AppAuth-Android.
 */
class AuthManager(private val context: Context) {


    var ip : String = "192.168.1.232"
    private val authService: AuthorizationService by lazy {
        val appAuthConfiguration = AppAuthConfiguration.Builder()
            .setConnectionBuilder(ConnectionBuilderForTesting.INSTANCE) // <-- Use the new class here
            .build()
        AuthorizationService(context, appAuthConfiguration)
    }


    private val clientId = "android-client"
    private val redirectUri = Uri.parse("com.example.myapp://oauth2redirect")
    private val scope = "openid profile"

    // Puoi anche usare la fetchFromIssuer se il server espone il discovery document
    private val serviceConfig = AuthorizationServiceConfiguration(
        Uri.parse("https://"+ ip +":9000/oauth2/authorize"), // authorization endpoint
        Uri.parse("https://"+ ip +":9000/oauth2/token")      // token endpoint
    )

    private val authRequest: AuthorizationRequest by lazy {
        AuthorizationRequest.Builder(
            serviceConfig,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri
        )
            .setScopes(scope)
            .build()
    }

    /**
     * Avvia il flusso di login aprendo il browser.
     */
    fun startLogin(activity: Activity, requestCode: Int) {
        val authIntent: Intent = authService.getAuthorizationRequestIntent(authRequest)
        activity.startActivityForResult(authIntent, requestCode)
    }

    /**
     * Gestisce la risposta di autorizzazione e scambia il codice con i token.
     */
    fun handleAuthResponse(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        expectedRequestCode: Int,
        onSuccess: (tokenResponse: TokenResponse, authState: AuthState) -> Unit,
        onError: (AuthorizationException?) -> Unit
    ) {
        if (requestCode == expectedRequestCode) {
            if (data == null) {
                Log.w("AuthManager", "handleAuthResponse: intent data is null")
                onError(null)
                return
            }

            val resp = AuthorizationResponse.fromIntent(data)
            val ex = AuthorizationException.fromIntent(data)

            Log.i("AuthManager", "AuthorizationResponse = $resp")
            Log.i("AuthManager", "AuthorizationException = $ex")

            if (resp != null) {
                val tokenRequest = resp.createTokenExchangeRequest()
                Log.i("AuthManager", "TokenRequest: $tokenRequest")
                authService.performTokenRequest(tokenRequest) { tokenResp, tokenEx ->
                    Log.i("AuthManager", "TokenResponse = $tokenResp")
                    Log.i("AuthManager", "TokenException = $tokenEx")

                    if (tokenResp != null) {
                        val authState = AuthState(resp, tokenResp, tokenEx)
                        // persistilo (vedi punto 2)
                        onSuccess(tokenResp, authState)
                    } else {
                        onError(tokenEx)
                    }
                }
            } else {
                onError(ex)
            }
        }
    }


    fun getValidAccessToken(
        storage: AuthStateStorage,
        onSuccess: (String) -> Unit,
        onError: (Exception?) -> Unit
    ) {
        val state = storage.readAuthState()
        if (state == null) {
            onError(Exception("AuthState non trovato"))
            return
        }

        state.performActionWithFreshTokens(authService) { accessToken, idToken, ex ->
            if (ex != null) {
                onError(ex)
            } else if (accessToken != null) {
                // aggiorna lo storage con l’AuthState aggiornato (dopo refresh)
                storage.writeAuthState(state)
                onSuccess(accessToken)
            } else {
                onError(Exception("Access token non disponibile"))
            }
        }
    }
}
