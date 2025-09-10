package com.example.e20frontendmobile.activities.evento

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e20frontendmobile.R
import com.example.e20frontendmobile.composables.CustomTextField
import com.example.e20frontendmobile.composables.IconButtonType1
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@Preview
@Composable
fun createEventPreview(){
    E20FrontendMobileTheme {
        createEvent()
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun createEvent(){
    val scrollState = rememberScrollState()

    var titolo by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var posti by remember { mutableIntStateOf(0) }
    var prezzo by remember { mutableFloatStateOf(0f) }
    var ageRestricted by remember { mutableStateOf(false) }
    var nominativo by remember { mutableStateOf(false) }
    var riutilizzabile by remember { mutableStateOf(false) }
    var descrizione by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf<String?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

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

                    selectedDate = localDate.toString()
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
                selectedTime = String.format(
                    "%02d:%02d",
                    timePickerState.hour,
                    timePickerState.minute
                )
                showTimePicker = false
            }
        )
    }

    Column (
        modifier = Modifier
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ){
        Box(contentAlignment = Alignment.BottomEnd){
            Image(
                /*bitmap = event.image?.asImageBitmap() ?: ,*/
                painter = painterResource(id = R.drawable.images),
                contentDescription = "Image of ${"event.id"}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
            )
            IconButtonType1(
                onClick = { },
                icon = Icons.Default.Edit,
                iconDescription = "",
                iconSize = 20.dp,
                modifier = Modifier.padding(10.dp)
            )
        }
        Column (
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(15.dp, 10.dp, 15.dp, 0.dp)
        ){
            Text(
                "Titolo",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )

            CustomTextField(
                value = titolo,
                onValueChange = { titolo = it },
                placeholder = "Titolo",
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,

            ) {

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        "Data",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row {
                        CustomTextField(
                            value = selectedDate ?: "",
                            onValueChange = { selectedDate = it },
                            placeholder = "Data",
                            singleLine = true,
                            modifier = Modifier.width(120.dp),
                            readOnly = true
                        )

                        IconButtonType1(
                            onClick = {showDatePicker = true},
                            icon = Icons.Default.DateRange,
                            iconDescription = "",
                            iconSize = 20.dp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Column {
                    Text(
                        "Orario",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row {
                        CustomTextField(
                            value = selectedTime ?: "",
                            onValueChange = { selectedTime = it },
                            placeholder = "Orario",
                            singleLine = true,
                            modifier = Modifier.width(120.dp),
                            readOnly = true
                        )

                        IconButtonType1(
                            onClick = {showTimePicker = true},
                            icon = Icons.Default.ShoppingCart,
                            iconDescription = "",
                            iconSize = 20.dp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

            }

            Column {
                Text(
                    "Location",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall
                )

                Row {
                    CustomTextField(
                        value = location,
                        onValueChange = { location = it },
                        placeholder = "Location",
                        singleLine = true,
                        modifier = Modifier.width(300.dp)
                    )

                    IconButtonType1(
                        onClick = { },
                        icon = Icons.Default.Place,
                        iconDescription = "",
                        iconSize = 20.dp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(vertical = 18.dp))


            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,

                ) {

                Column (modifier = Modifier.fillMaxWidth(0.5f)){
                    Text(
                        "Posti",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row {
                        CustomTextField(
                            value = posti.toString(),
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() }) {
                                    posti = newValue.toInt()
                                } },
                            placeholder = "Posti",
                            singleLine = true,
                            modifier = Modifier.width(300.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                Column {
                    Text(
                        "Prezzo",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row {
                        CustomTextField(
                            value = prezzo.toString(),
                            onValueChange = { newValue->
                                if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    prezzo = newValue.toFloat()
                                }
                            },
                            placeholder = "Prezzo",
                            singleLine = true,
                            modifier = Modifier.width(300.dp)
                        )
                    }
                }



            }

            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(vertical = 18.dp))

            var checked by remember { mutableStateOf(true) }

            Row (verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Vietato ai minori",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .width(250.dp)
                )
                Switch(
                    checked = ageRestricted,
                    onCheckedChange = {
                        ageRestricted = it
                    },
                )
            }

            Row (verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Biglietto Nominativo",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .width(250.dp)
                )
                Switch(
                    checked = nominativo,
                    onCheckedChange = {
                        nominativo = it
                    }
                )
            }


            Row (verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Biglietto Riutilizzabile",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .width(250.dp)
                )
                Switch(
                    checked = riutilizzabile,
                    onCheckedChange = {
                        riutilizzabile = it
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Column {
                Text(
                    "Descrizione",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall
                )

                Row {
                    CustomTextField(
                        value = descrizione,
                        onValueChange = { descrizione = it },
                        placeholder = "Descrizione",
                        singleLine = true,
                        modifier = Modifier.width(300.dp)
                    )
                }
            }



            Spacer(modifier = Modifier.height(60.dp))



            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(80.dp),
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




//@Composable
//@PreviewScreenSizes
//fun previewButton(){
//    E20FrontendMobileTheme {
//        CustomTextField()
//    }
//}