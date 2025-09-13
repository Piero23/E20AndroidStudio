package com.example.e20frontendmobile.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class CreateEventViewModel : ViewModel() {


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

        if (location == "") {
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

    fun edit(){
//        titolo = selectedEvent?.title ?: ""
//        location = selectedEvent?.locationId.toString() ?: ""
//        posti = selectedEvent?.posti.toString() ?: ""
//        prezzo = selectedEvent?.prezzo.toString() ?: ""
//        ageRestricted = selectedEvent?.restricted ?: false
//        nominativo = selectedEvent?.b_nominativo ?: false
//        riutilizzabile = selectedEvent?.b_riutilizzabile ?: false
//        descrizione = selectedEvent?.description ?: ""
////        //TODO RISOLVERE DATA
//        selectedDate = selectedEvent?.date.toString() ?: ""
//        selectedTime = selectedEvent?.date.toString() ?: ""
//
//        editing = true
    }



    fun sendEvent(context : Context) {
        nomeSbagliato = false
        locationSbagliata = false
        prezzoSbagliato = false
        postiSbagliati = false
        dataSbagliata = false
        orarioSbagliato = false

//        verify()
        if( !(dataSbagliata || postiSbagliati || prezzoSbagliato || locationSbagliata || nomeSbagliato || orarioSbagliato)) {
            viewModelScope.launch {
//                selectedEventLocation?.let {
//                    EventService(context).create(
//                        Event(
//                            1,
//                            descrizione,
//                            title = titolo,
//                            date = LocalDateTime.parse(selectedDate+"T"+selectedTime),
//                            locationId = it.id!!,
//                            posti = posti.toInt(),
//                            prezzo = prezzo.toDouble(),
//                            restricted = ageRestricted,
//                            organizzatore = UtenteService(context).getUtenteSub() ?: "no",
//                            b_riutilizzabile = riutilizzabile,
//                            b_nominativo = nominativo
//                        )
//                    )
//                }
            }
            //TODO clear campi
        }
    }


    fun clearSelection() {
        titolo =  ""
        location =  ""
        posti =  ""
        prezzo =  ""
        ageRestricted =  false
        nominativo =  false
        riutilizzabile =  false
        descrizione = ""
        selectedDate =  ""
        selectedTime = ""
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