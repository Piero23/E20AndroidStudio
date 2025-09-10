package com.example.e20frontendmobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e20frontendmobile.data.apiService.EventoLocation.EventService
import com.example.e20frontendmobile.model.Event
import kotlinx.coroutines.launch
import android.content.Context
import android.graphics.Bitmap

class EventViewModel : ViewModel() {

    // Evento selezionato
    var selectedEvent by mutableStateOf<Event?>(null)
        private set

    // Lista risultati ricerca
    var items by mutableStateOf<List<Event>>(emptyList())
        private set

    var query by mutableStateOf("")
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var eventImage by mutableStateOf<Bitmap?>(null)
        private set

    fun selectEvent(event: Event) {
        selectedEvent = event
    }

    fun clearSelection() {
        selectedEvent = null
    }

    fun search(context: Context, newQuery: String) {
        query = newQuery
        loading = true
        error = null

        viewModelScope.launch {
            try {
                val eventService = EventService(context)
                items = eventService.search(newQuery)
            } catch (e: Exception) {
                error = e.message ?: "Impossibile Caricare Evento"
                items = emptyList()
            } finally {
                loading = false
            }
        }
    }

    private val _eventImages = mutableMapOf<Long, Bitmap?>()
    fun getCachedImage(eventId: Long): Bitmap? = _eventImages[eventId]

    fun fetchImage(context: Context, eventId: Long, onResult: (Bitmap?) -> Unit) {
        // se già c’è in cache, restituisci subito
        _eventImages[eventId]?.let {
            onResult(it)
            return
        }

        viewModelScope.launch {
            val eventService = EventService(context)
            val bitmap = eventService.getImage(eventId)
            _eventImages[eventId] = bitmap
            onResult(bitmap)
        }
    }

    fun refresh(context: Context) {
        search(context, query)
    }
}

