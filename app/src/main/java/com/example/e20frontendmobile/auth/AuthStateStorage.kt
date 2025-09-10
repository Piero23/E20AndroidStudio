package com.example.e20frontendmobile.auth

import android.content.Context
import android.util.Log
import net.openid.appauth.AuthState
import org.json.JSONException
import androidx.core.content.edit

class AuthStateStorage(private val context: Context) {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    data class UserInfo(
        val sub: String,
        val roles: List<String>
    )

    fun writeAuthState(state: AuthState) {
        prefs.edit {
            putString("auth_state", state.jsonSerializeString())
        }
    }

    fun readAuthState(): AuthState? {
        val stateJson = prefs.getString("auth_state", null)
        return if (stateJson != null) {
            try {
                AuthState.jsonDeserialize(stateJson)
            } catch (ex: JSONException) {
                Log.e("AuthStateStorage", "Errore nel deserializzare AuthState", ex)
                null
            }
        } else {
            null
        }
    }


    fun decodeJwt(token: String): Map<String, Any>? {

        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null

            val payloadJson = String(
                android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE)
            )

            val jsonObject = org.json.JSONObject(payloadJson)
            val map = mutableMapOf<String, Any>()
            jsonObject.keys().forEach { key ->
                map[key] = jsonObject.get(key)
            }
            map
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getUserInfo(): UserInfo? {
        val token = readAuthState()?.accessToken ?: return null
        val claims = decodeJwt(token)

        val sub = claims?.get("sub") as? String ?: return null

        val roles: List<String> = when (val raw = claims["roles"]) {
            is org.json.JSONArray -> {
                val list = mutableListOf<String>()
                for (i in 0 until raw.length()) {
                    list.add(raw.getString(i))
                }
                list
            }
            is String -> listOf(raw)
            is List<*> -> raw.map { it.toString() }
            else -> emptyList()
        }

        return UserInfo(sub, roles)
    }


    fun clearAuthState() {
        prefs.edit { remove("auth_state") }
    }

    fun getAccessToken(): String? {
        val state = readAuthState() ?: return null
        return state.accessToken
    }
}
