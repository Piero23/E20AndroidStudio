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
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableIntStateOf
import com.example.e20frontendmobile.R
import com.example.e20frontendmobile.data.apiService.Utente.UtenteService
import kotlin.collections.getValue
import kotlin.collections.set
import kotlin.collections.setValue

class EventViewModel : ViewModel() {

    var spotsLeft: Int by mutableIntStateOf(-1)

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


    fun refresh(context: Context) {
        search(context, query)
    }

    fun spotsLeft(context: Context){
        viewModelScope.launch {
            loading = true
            try {
                val eventService = EventService(context)
                spotsLeft = eventService.spotsLeft(selectedEvent?.id ?: -1)
            } catch (e: Exception) {
                spotsLeft = -1
            } finally {
                loading = false
            }
        }
    }

    fun checkEventManager(context: Context): Boolean{
        return selectedEvent?.organizzatore == UtenteService(context).getUtenteSub()
    }



    // Cached Image

    private val mappaImmagini: MutableMap<Long, Bitmap> = mutableMapOf()

    suspend fun getEventImage(eventoId : Long , context: Context): Bitmap? {

        if (mappaImmagini.containsKey(eventoId)) {
            return mappaImmagini[eventoId]!!
        }

        val eventService = EventService(context)
        val bitmap = eventService.getImage(eventoId)
            ?: BitmapFactory.decodeResource(context.resources, R.drawable.noimage)

        addToMappaImmagini(eventoId, bitmap)
        return bitmap
    }

    fun addToMappaImmagini(id: Long, bitmap : Bitmap){

        if(mappaImmagini.contains(id))
            return

        mappaImmagini.put(id,bitmap)
    }

    fun clearMappaImmagini(){
        mappaImmagini.clear()
    }

}