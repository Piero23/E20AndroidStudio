package com.example.e20frontendmobile.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e20frontendmobile.data.apiService.EventoLocation.LocationService
import com.example.e20frontendmobile.model.Address
import com.example.e20frontendmobile.model.Location
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {


    //--------Generale
    var loading by mutableStateOf(false)
    var selectedEventLocation: Location? by mutableStateOf(null)
    var selectedLocationAddress: Address? by mutableStateOf(null)


    suspend fun getLocationFromEvent(context: Context , locationId : Long){

            loading = true
            try {
                val locationService = LocationService(context)
                selectedEventLocation=locationService.findById(locationId)
                selectedLocationAddress = locationService.getAddress(selectedEventLocation?.position.toString())
            } catch (e: Exception) {
                selectedEventLocation = null
            } finally {
                loading = false
            }

    }

    //------------Creazione
    var locations by mutableStateOf<List<Location>>(emptyList())
        private set
    var locationsAdress by mutableStateOf<List<Address>>(emptyList())
        private set


    fun clearLocations(){
        locations = emptyList()
    }


    //------------Ricerca
     fun searchLocations(context: Context, query: String){
        loading = true
        viewModelScope.launch {
            try {

                val locationService = LocationService(context)
                if (locationService.search(query).isNotEmpty()) {
                    locations = locationService.search(query)
                    locationsAdress = locations.mapNotNull { loc ->
                        loc.position?.let { locationService.getAddress(it) }
                    }
                }
            } catch (e: Exception) {
                locations = emptyList()
            } finally {
                loading = false
            }
        }
    }



}