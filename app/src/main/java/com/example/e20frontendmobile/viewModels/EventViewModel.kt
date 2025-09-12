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
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.net.toUri
import com.example.e20frontendmobile.R
import com.example.e20frontendmobile.data.apiService.Utente.UtenteService
import kotlinx.datetime.LocalDateTime
import java.io.File

class EventViewModel : ViewModel() {

    var nomeSbagliato by mutableStateOf(false)
    var locationSbagliata by mutableStateOf(false)
    var prezzoSbagliato by mutableStateOf(false)
    var postiSbagliati by mutableStateOf(false)
    var dataSbagliata by mutableStateOf(false)

    var orarioSbagliato by mutableStateOf(false)

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
        titolo =  ""
        location =  ""
        posti =  ""
        prezzo =  ""
        ageRestricted =  false
        nominativo =  false
        riutilizzabile =  false
        descrizione = ""
//        //TODO RISOLVERE DATA
        selectedDate =  ""
        selectedTime = ""
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
        _eventImages[eventId]?.let {
            onResult(it)
            return
        }

        viewModelScope.launch {
            val eventService = EventService(context)
            val bitmap = eventService.getImage(eventId) ?: BitmapFactory.decodeResource(context.resources, R.drawable.images)
            _eventImages[eventId] = bitmap
            onResult(bitmap)
        }
    }

    fun refresh(context: Context) {
        search(context, query)
    }

    //-----------------------CREAZIONE EVENTO------------------------------
    //TODO inizializzare alle variabili di evento


    var ciao = "Palle"
    var titolo: String by   mutableStateOf("")



    var location by  mutableStateOf("")
    var posti by   mutableStateOf("")
    var prezzo by   mutableStateOf("")
    var ageRestricted by mutableStateOf(false)
    var nominativo by  mutableStateOf(false)
    var riutilizzabile by  mutableStateOf(false)
    var descrizione by mutableStateOf("")
    var selectedDate by  mutableStateOf<String?>(null)
    var selectedTime by mutableStateOf<String?>(null)

    fun verify(){
        if (titolo == "") {
            nomeSbagliato = true
        }

        if (location == "") {
            locationSbagliata = true
        }

        if (prezzo == "") {
            prezzoSbagliato = true
        }

        if (posti == "") {
            postiSbagliati = true
        }


        if (selectedDate == null) {
            dataSbagliata = true
        }

        if (selectedTime == null){
            orarioSbagliato = true
        }
    }

    fun resetDati(){
        nomeSbagliato = false
        locationSbagliata = false
        prezzoSbagliato = false
        postiSbagliati = false
        dataSbagliata = false
        orarioSbagliato = false
    }

    fun sendEvent(context : Context){
        resetDati()

        verify()
        if( !(dataSbagliata || postiSbagliati || prezzoSbagliato || locationSbagliata || nomeSbagliato || orarioSbagliato)) {
            viewModelScope.launch {
                    EventService(context).create(
                        Event(
                            1,
                            descrizione,
                            title = titolo,
                            date = LocalDateTime.parse(selectedDate+"T"+selectedTime),
                            locationId = location.toLong(),
                            posti = posti.toInt(),
                            prezzo = prezzo.toDouble(),
                            restricted = ageRestricted,
                            organizzatore = UtenteService(context).getUtenteSub() ?: "no",
                            b_riutilizzabile = riutilizzabile,
                            b_nominativo = nominativo
                        )
                    )
            }
            //TODO clear campi
        }
    }

    //--------------------------AGGIORNAMENTO-------------------
    var editing by  mutableStateOf(false)

    fun edit(){
        titolo = selectedEvent?.title ?: ""
        location = selectedEvent?.locationId.toString() ?: ""
        posti = selectedEvent?.posti.toString() ?: ""
        prezzo = selectedEvent?.prezzo.toString() ?: ""
        ageRestricted = selectedEvent?.restricted ?: false
        nominativo = selectedEvent?.b_nominativo ?: false
        riutilizzabile = selectedEvent?.b_riutilizzabile ?: false
        descrizione = selectedEvent?.description ?: ""
//        //TODO RISOLVERE DATA
        selectedDate = selectedEvent?.date.toString() ?: ""
        selectedTime = selectedEvent?.date.toString() ?: ""

        editing = true
    }

    //--------------------------IMMAGINI------------------------
    var createSelectedImage by mutableStateOf("".toUri())
    fun uriToFile(context: Context): File {
        val inputStream = context.contentResolver.openInputStream(createSelectedImage)!!
        val file = File(context.cacheDir, "upload_image.jpg")
        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return file
    }

    fun bitmapToFile(context: Context, bitmap: Bitmap): File {
        // Crea un file nella cache dell’app
        val file = File(context.cacheDir, "upload_image.jpg")

        // Scrive il bitmap nel file
        file.outputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        }

        return file
    }

    fun bitmapToUri(context: Context, bitmap: Bitmap?): Uri {
        val file = File(context.cacheDir, "upload_image.jpg")
        file.outputStream().use { output ->
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, output) ?: ""
        }

        return Uri.fromFile(file)
    }
}

