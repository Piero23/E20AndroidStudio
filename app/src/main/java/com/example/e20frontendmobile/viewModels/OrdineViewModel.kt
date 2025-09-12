package com.example.e20frontendmobile.viewModels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e20frontendmobile.apiService.OrdineService
import com.example.e20frontendmobile.data.apiService.Utente.UtenteService
import com.example.e20frontendmobile.model.Ordine
import kotlinx.coroutines.launch

class OrdineViewModel : ViewModel() {

    var ordiniUtente by mutableStateOf<List<Ordine>?>(emptyList())

    fun getOrdini(context : Context){
        var uuid = UtenteService(context).getUtenteSub()

        viewModelScope.launch {
            ordiniUtente = OrdineService(context).findAllByUtente(uuid)
        }
    }


}