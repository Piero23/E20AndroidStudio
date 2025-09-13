package com.example.e20frontendmobile.activities.evento

import android.content.Context
import android.graphics.drawable.Icon
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.e20frontendmobile.composables.LocationPickerPopup
import com.example.e20frontendmobile.model.Event
import com.example.e20frontendmobile.viewModels.CreateEventViewModel
import com.example.e20frontendmobile.viewModels.EventViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Calendar
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import com.example.e20frontendmobile.R
import com.example.e20frontendmobile.composables.CustomTextField
import com.example.e20frontendmobile.composables.IconButtonType1
import com.example.e20frontendmobile.viewModels.LocationViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun timeDatePicker() {

}


@Composable
fun setImage(
    evento: Event?,
    createEventViewModel: CreateEventViewModel,
    context: Context,
    eventViewModel: EventViewModel
) {

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            createEventViewModel.createSelectedImage = uri
        } else {
            Toast.makeText(context, "Errore: ${"Creazione immagine"}", Toast.LENGTH_LONG).show()

        }
    }

    if (evento != null) {

        LaunchedEffect(evento) {
            createEventViewModel.createSelectedImage = createEventViewModel.bitmapToUri(
                context,
                (eventViewModel.getEventImage(evento.id, context))
            )
        }
    }

    if (createEventViewModel.createSelectedImage.path != "") {
        AsyncImage(
            model = createEventViewModel.createSelectedImage,
            contentDescription = "Image of ${"event.id"}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
        )
    } else {
        Image(
            painter = painterResource(R.drawable.photomode_18072025_201346),
            contentDescription = "Placeholder",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
    }

    IconButtonType1(
        onClick = {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        icon = Icons.Default.Edit,
        iconDescription = "",
        iconSize = 20.dp,
        modifier = Modifier.padding(10.dp)
    )
}


@Composable
fun textFieldCreateEvent(titolo: String, controllo: Boolean) {

    Row {
        Text(
            titolo,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge
        )
        if (controllo) {
            Text(
                "*",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun locationSection(
    createEventViewModel: CreateEventViewModel, context: Context,
    locationViewModel: LocationViewModel = viewModel()
) {

    var showLocationPicker by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    if (showLocationPicker) {
        LocationPickerPopup { showLocationPicker = false }
    }
    val coroutineScope = rememberCoroutineScope()
    var locationName by remember { mutableStateOf("") }

    Column {
        textFieldCreateEvent("Location", createEventViewModel.locationSbagliata)

        Row {
            CustomTextField(
                value = locationName,
                onValueChange = {
                    locationName = it
                    locationViewModel.searchLocations(context, it)
                    showSearch = true
                    createEventViewModel.location = ""
                },
                placeholder = "Location",
                singleLine = true,
                modifier = Modifier.width(300.dp)
            )

            IconButtonType1(
                onClick = { showLocationPicker = true },
                icon = Icons.Default.Place,
                iconDescription = "",
                iconSize = 20.dp,
                modifier = Modifier.padding(10.dp)
            )
        }

        if (locationName.isNotEmpty() && showSearch) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 2.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {

                if (locationViewModel.locations.isNotEmpty()) {

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp)
                    ) {
                        itemsIndexed(locationViewModel.locations) { index, option ->
                            val address = locationViewModel.locationsAdress.getOrNull(index)

                            if (address != null) {
                                Text(
                                    text = """${option.nome}
                                                    |${address?.road}
                                                    |${address?.village}, ${address.postcode}""".trimMargin()
                                        ?: "",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            locationName = option.nome ?: ""
                                            createEventViewModel.location = option.id.toString()
                                            locationViewModel.clearLocations()
                                            showSearch = false
                                        }
                                        .padding(10.dp)
                                )
                                if (index != locationViewModel.locations.size - 1) {
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun createEvent(evento: Event?, eventViewModel: EventViewModel = viewModel()) {

    var createEventViewModel: CreateEventViewModel = viewModel()

    //----------------------Edit
    //TODO mettere nel model
    val context = LocalContext.current

    if (evento != null) {
        LaunchedEffect(evento) {
            createEventViewModel.edit(evento)
        }
    }

    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    //----------------------Location

    //----------------------Calendario
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    val localDate = Instant.fromEpochMilliseconds(it)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date

                    createEventViewModel.selectedDate = localDate.toString()
                }
            },
            onDismiss = { showDatePicker = false }
        )
    }


    if (showTimePicker) {
        TimePickerDialog(
            state = timePickerState,
            onDismiss = { showTimePicker = false },
            onConfirm = {
                createEventViewModel.selectedTime = String.format(
                    "%02d:%02d",
                    timePickerState.hour,
                    timePickerState.minute
                )
                showTimePicker = false
            }
        )
    }


    //----------------------Compose


    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            setImage(evento, createEventViewModel, context, eventViewModel)
        }


        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(15.dp, 10.dp, 15.dp, 0.dp)
        ) {
            textFieldCreateEvent("Titolo", createEventViewModel.nomeSbagliato)

            CustomTextField(
                value = createEventViewModel.titolo,
                onValueChange = { createEventViewModel.titolo = it },
                placeholder = "Titolo",
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,

                ) {

                Column {
                    textFieldCreateEvent("Data", createEventViewModel.dataSbagliata)
                    CustomTextField(
                        value = createEventViewModel.selectedDate ?: "",
                        onValueChange = { createEventViewModel.selectedDate = it },
                        placeholder = "Data",
                        singleLine = true,
                        modifier = Modifier.width(120.dp),
                        readOnly = true
                    )
                }
                IconButtonType1(
                    onClick = { showDatePicker = true },
                    icon = Icons.Default.DateRange,
                    iconDescription = "",
                    iconSize = 20.dp,
                    modifier = Modifier.padding(10.dp)
                )

                Column {
                    textFieldCreateEvent("Ora", createEventViewModel.orarioSbagliato)
                    CustomTextField(
                        value = createEventViewModel.selectedTime ?: "",
                        onValueChange = { createEventViewModel.selectedTime = it },
                        placeholder = "Ora",
                        singleLine = true,
                        modifier = Modifier.width(120.dp),
                        readOnly = true
                    )
                }
                //TODO fix
                IconButtonType1(
                    onClick = { showTimePicker = true },
                    icon = Icons.Filled.ShoppingCart,
                    iconDescription = "",
                    iconSize = 20.dp,
                    modifier = Modifier.padding(10.dp)
                )
            }

            locationSection(createEventViewModel, context)

            Spacer(
                modifier = Modifier
                    .height(30.dp)
                    .padding(vertical = 18.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,

                ) {

                Column {
                    textFieldCreateEvent("Posti", createEventViewModel.postiSbagliati)
                    CustomTextField(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = createEventViewModel.posti,
                        onValueChange = { newValue ->
                            if (newValue.matches(Regex("^\\d*\\.?\\d*\$")) || newValue.isEmpty()) {
                                createEventViewModel.posti = newValue
                            }
                        },
                        placeholder = "0",
                        singleLine = true,
                        modifier = Modifier.width(120.dp),
                    )
                }

                Column {
                    textFieldCreateEvent("Prezzo", createEventViewModel.prezzoSbagliato)
                    CustomTextField(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = createEventViewModel.prezzo,
                        onValueChange = { newValue ->
                            if (newValue.matches(Regex("^\\d*\\.?\\d*\$")) || newValue.isEmpty()) {
                                createEventViewModel.prezzo = newValue
                            }
                        },
                        placeholder = "0.0",
                        singleLine = true,
                        modifier = Modifier.width(120.dp),
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            }
            Column (verticalArrangement = Arrangement.Top ){
                Row (verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Biglietto nominativo",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(250.dp)
                    )
                    Switch(
                        checked = createEventViewModel.nominativo,
                        onCheckedChange = {
                            createEventViewModel.nominativo = it
                        },
                    )
                }

                if(createEventViewModel.nominativo){
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Vietato ai minori",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .width(250.dp)
                        )
                        Switch(
                            checked = createEventViewModel.ageRestricted,
                            onCheckedChange = {
                                createEventViewModel.ageRestricted = it
                            },
                        )
                    }
                }

                Row (verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Biglietto riutilizzabile",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(250.dp)
                    )
                    Switch(
                        checked = createEventViewModel.riutilizzabile,
                        onCheckedChange = {
                            createEventViewModel.riutilizzabile = it
                        },
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))

                Button(
                onClick = {
                    createEventViewModel.sendEvent(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(20)
            ){
                Row (verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(text = "Crea Evento",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
            }
        }
    }

//
//
//
//
//
//
//
//
//
//
//        }
//    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    state: TimePickerState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleziona un orario") },
        text = { TimePicker(state = state) },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}