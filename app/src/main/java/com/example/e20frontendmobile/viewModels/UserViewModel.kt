package com.example.e20frontendmobile.viewModels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e20frontendmobile.apiService.PreferitiService
import com.example.e20frontendmobile.data.apiService.EventoLocation.EventService
import com.example.e20frontendmobile.data.apiService.Utente.UtenteService
import com.example.e20frontendmobile.data.auth.AuthStateStorage
import com.example.e20frontendmobile.model.Event
import com.example.e20frontendmobile.model.Utente
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    var selectedUserProfile by mutableStateOf<Utente?>(null)
    var items by mutableStateOf<List<Utente>>(emptyList())
        private set

    var query by mutableStateOf("")
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun setDisplayableUser(utente: Utente){
        selectedUserProfile = utente
    }

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


    fun salvaPreferito(context: Context , eventoId : Long){
        viewModelScope.launch {
            PreferitiService(context).aggiungiAiPreferiti(
                AuthStateStorage(context).getUserInfo()?.sub,
                eventoId
            )
        }
    }

    fun removePreferiti(context: Context ,  eventoId : Long){
        viewModelScope.launch {
            PreferitiService(context).rimuoviDaiPreferiti(
                AuthStateStorage(context).getUserInfo()?.sub,
                eventoId
            )
        }
    }

    fun checkIfPreferito(context: Context , eventoId : Long): Boolean {

        val storage =  AuthStateStorage(context)
        val userInfo = storage.getUserInfo()

        var allpreferiti: List<Event> = listOf()
        if (userInfo?.sub!=null){
            viewModelScope.launch {
                allpreferiti = PreferitiService(context).getAllPreferiti(userInfo?.sub)
            }
            for (item in allpreferiti){
                if (item.id== eventoId) return true
            }
        }
        return false
    }

    fun followUser(context: Context, username: String) {
        viewModelScope.launch {
            UtenteService(context).aggiungiAiSeguiti(username)
        }
    }
}