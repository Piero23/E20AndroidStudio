import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.e20frontendmobile.auth.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.openid.appauth.AuthState
import net.openid.appauth.TokenResponse

object AuthModule {

    private lateinit var authManager: AuthManager
    const val RC_AUTH = 9001

    // Stati osservabili
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _authState = MutableStateFlow<AuthState?>(null)
    val authState: StateFlow<AuthState?> = _authState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun initialize(context: Context) {
        if (!::authManager.isInitialized) {
            authManager = AuthManager(context.applicationContext)
        }
    }

    fun login(activity: Activity) {
        _error.value = null
        _isLoading.value = true
        authManager.startLogin(activity, RC_AUTH)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode != RC_AUTH) return false

        _isLoading.value = false

        if (resultCode == Activity.RESULT_OK && data != null) {
            authManager.handleAuthResponse(
                requestCode,
                resultCode,
                data,
                RC_AUTH,
                onSuccess = { tokenResponse: TokenResponse, state: AuthState ->

                    val token = tokenResponse.accessToken
                    println("Access Token completo: $token")
                },
                onError = { ex ->
                    println("Errore autenticazione: ${ex?.errorDescription}")
                }
            )
        } else if (resultCode == Activity.RESULT_CANCELED) {
            _error.value = "Login annullato"
        }

        return true
    }

    fun logout() {
        _authState.value = null
        _isAuthenticated.value = false
        _error.value = null
    }

    fun getAccessToken(): String? {
        return _authState.value?.accessToken
    }
}