package com.example.e20frontendmobile.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e20frontendmobile.data.apiService.EventoLocation.EventService
import com.example.e20frontendmobile.data.apiService.Utente.UtenteService
import com.example.e20frontendmobile.model.Event
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import java.io.File

class CreateEventViewModel : ViewModel() {


    var titolo: String by   mutableStateOf("")

    var selectedEvent  by mutableStateOf<Event?>(null)
    var location by  mutableLongStateOf(-1)
    var posti by   mutableStateOf("")
    var prezzo by   mutableStateOf("")
    var ageRestricted by mutableStateOf(false)
    var nominativo by  mutableStateOf(false)
    var riutilizzabile by  mutableStateOf(false)
    var descrizione by mutableStateOf("")
    var selectedDate by  mutableStateOf<String?>(null)
    var selectedTime by mutableStateOf<String?>(null)


    var nomeSbagliato by mutableStateOf(false)
    var locationSbagliata by mutableStateOf(false)
    var prezzoSbagliato by mutableStateOf(false)
    var postiSbagliati by mutableStateOf(false)
    var dataSbagliata by mutableStateOf(false)
    var orarioSbagliato by mutableStateOf(false)



    fun verify(){
        if (titolo == "") {
            nomeSbagliato = true
        }

        if (location == -1L) {
            locationSbagliata = true
        }

        if (prezzo == "") {
            prezzoSbagliato = true
        }

        if (posti == "") {
            postiSbagliati = true
        }


        if (selectedDate == null) { dataSbagliata = true }

        if (selectedTime == null){
            orarioSbagliato = true
        }
    }

    var editing by mutableStateOf(false)

    fun edit(evento : Event){
        titolo = evento.title
        location = evento.locationId
        posti = evento.posti.toString()
        prezzo = evento.prezzo.toString()
        ageRestricted = evento.restricted
        nominativo = evento.b_nominativo
        riutilizzabile = evento.b_riutilizzabile
        descrizione = evento.description
        
        
        
        selectedDate = evento.date.toString().split("T")[0]
        selectedTime = evento.date.toString().split("T")[1]

        editing = true
        selectedEvent = evento
    }



    fun sendEvent(context : Context): Boolean {
        nomeSbagliato = false
        locationSbagliata = false
        prezzoSbagliato = false
        postiSbagliati = false
        dataSbagliata = false
        orarioSbagliato = false

        var status = false

        verify()
        if( !(dataSbagliata || postiSbagliati || prezzoSbagliato || locationSbagliata || nomeSbagliato || orarioSbagliato)) {
            viewModelScope.launch {
                
                
                try {
                    println("MUCCA33")
                    var evento = Event(
                        1,
                        descrizione,
                        title = titolo,
                        date = LocalDateTime.parse(selectedDate + "T" + selectedTime),
                        locationId = location,
                        posti = posti.toInt(),
                        prezzo = prezzo.toDouble(),
                        restricted = ageRestricted,
                        organizzatore = UtenteService(context).getUtenteSub() ?: "no",
                        b_riutilizzabile = riutilizzabile,
                        b_nominativo = nominativo
                    )

                    println(evento.toString())

                    var returningEvent : Event?
                    if (!editing) {
                        returningEvent = EventService(context).create(evento)
                    }else{
                        evento.id = selectedEvent?.id!!
                        returningEvent = EventService(context).edit(evento)
                    }

                    //EventService(context).uploadImageEvento(returningEvent?.id ?: 1, uriToFile(context)) //TODO fixare che creaevento non mette immagine di default
                    Toast.makeText(context, "Evento creato correttamente", Toast.LENGTH_LONG).show()
                    status = true
                } catch (e: Exception) {
                    Toast.makeText(context, "Qualcosa è andato storto", Toast.LENGTH_LONG).show()
                }

            }
        }else
            Toast.makeText(context, "Errore nella creazione", Toast.LENGTH_LONG).show()
        return status
    }
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