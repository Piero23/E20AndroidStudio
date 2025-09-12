package com.example.e20frontendmobile.viewModels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e20frontendmobile.data.apiService.EventoLocation.EventService
import com.example.e20frontendmobile.data.apiService.Utente.UtenteService
import com.example.e20frontendmobile.model.Event
import com.example.e20frontendmobile.model.Utente
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    var items by mutableStateOf<List<Utente>>(emptyList())
        private set

    var query by mutableStateOf("")
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun search(context: Context, newQuery: String) {
        query = newQuery
        loading = true
        error = null

        viewModelScope.launch {
            try {
                val utenteService = UtenteService(context)
                items = utenteService.search(newQuery)
            } catch (e: Exception) {
                error = e.message ?: "Impossibile Caricare Utente"
                items = emptyList()
            } finally {
                loading = false
            }
        }
    }
}