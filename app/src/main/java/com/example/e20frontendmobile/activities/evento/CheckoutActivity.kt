package com.example.e20frontendmobile.activities.evento

import android.content.Intent
import android.net.Uri
import org.json.JSONObject
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.e20frontendmobile.composables.CustomTextField
import com.example.e20frontendmobile.composables.IconTextButtonType1
import com.example.e20frontendmobile.data.apiService.PagamentoService
import com.example.e20frontendmobile.model.Ticket
import com.example.e20frontendmobile.ui.theme.buttonGradientType1
import com.example.e20frontendmobile.viewModels.EventViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import kotlin.time.ExperimentalTime
import androidx.core.net.toUri

@OptIn(ExperimentalTime::class)
@Composable
fun ShowCheckout(navController: NavHostController, eventViewModel: EventViewModel){
    var tickets by rememberSaveable { mutableStateOf(listOf(Ticket())) }
    val scrollState = rememberScrollState()

    val event = eventViewModel.selectedEvent

    val context = LocalContext.current
    if (event == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Seleziona un evento")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 5.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Indietro"
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Checkout",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.size(48.dp))
        }
        HorizontalDivider(
            modifier = Modifier.padding(10.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            tickets.forEachIndexed { index, ticket ->
                NameTicket(
                    ticket = ticket,
                    number = index + 1,
                    onTicketChange = { updatedTicket ->
                        tickets = tickets.toMutableList().also { it[index] = updatedTicket }
                    },
                    idEvento = event.id
                )
                HorizontalDivider(modifier = Modifier.padding(10.dp))
            }
            IconTextButtonType1(
                onClick = {tickets = tickets + Ticket() },
                text =  "Aggiungi Biglietto",
                modifier = Modifier,
                withIcon = true,
                icon = Icons.Filled.Add,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Biglietti: ${tickets.size}")
                Text("Totale: ${tickets.size*event.prezzo}€")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(42, 42, 42, 255),
                    contentColor = Color.White
                ),
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val link = PagamentoService(context).checkout(
                                UUID.fromString("5a49d976-5b8c-47ed-8d9b-1e0a553a0d9d"),
                                "eur",
                                tickets
                            )

                            link?.let {
                                withContext(Dispatchers.Main) {
                                    val intent = Intent(Intent.ACTION_VIEW, link.toUri())
                                    context.startActivity(intent)
                                }
                            } ?: run {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Errore: link non disponibile", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Errore: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                            println("Errore: ${e.message}")
                        }
                    }
                }
            ){
                Text("Paga")
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun NameTicket(
    number: Int,
    ticket: Ticket,
    onTicketChange: (Ticket) -> Unit,
    idEvento: Long
) {
    ticket.idEvento=idEvento
    // stato locale del DatePicker per questo singolo ticket
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Biglietto $number",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        // Nome + Cognome
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 5.dp, 10.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CustomTextField(
                value = (ticket.nome ?: ""),
                onValueChange = { onTicketChange(ticket.copy(nome = it)) },
                placeholder = "Nome",
                singleLine = true,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 2.5.dp)
            )
            CustomTextField(
                value = (ticket.cognome ?: ""),
                onValueChange = { onTicketChange(ticket.copy(cognome = it)) },
                placeholder = "Cognome",
                singleLine = true,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 2.5.dp)
            )
        }

        // Email
        CustomTextField(
            value = (ticket.email ?: ""),
            onValueChange = { onTicketChange(ticket.copy(email = it)) },
            placeholder = "eMail",
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        Row(
            modifier = Modifier
                .padding(70.dp, 0.dp, 70.dp, 0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextField(
                value = (ticket.dataNascita ?: ""),
                onValueChange = { onTicketChange(ticket.copy(dataNascita = it)) },
                placeholder = "Data di nascita",
                singleLine = true,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 2.5.dp, end = 5.dp)
                    .weight(1f)
            )
            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(buttonGradientType1()),
                onClick = { showDatePicker = true }
            ) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "Scegli data",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Mostra il DatePicker solo per questo ticket
        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { millis ->
                    millis?.let {
                        val localDate = Instant.fromEpochMilliseconds(it)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date
                        onTicketChange(ticket.copy(dataNascita = localDate.toString()))
                    }
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

