package com.example.e20frontendmobile.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.e20frontendmobile.composables.IconTextButtonType1
import com.example.e20frontendmobile.ui.theme.buttonGradientType1


data class Ticket(
    var nome: String = "",
    var cognome: String = "",
    var email: String = "",
    var dataNascita: String = ""
)

@Composable
fun ShowCheckout(navController: NavHostController){
    var tickets by rememberSaveable { mutableStateOf(listOf(Ticket())) }
    val scrollState = rememberScrollState()

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
                NameTicket(ticket = ticket, onTicketChange = { updatedTicket ->
                    tickets = tickets.toMutableList().also { it[index] = updatedTicket }
                }, number = index + 1)
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
                Text("Totale: 100000€") //TODO da calcolare
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(42, 42, 42, 255),
                    contentColor = Color.White
                ),
                onClick = {}
            ){
                Text("Paga")
            }
        }
    }
}

@Composable
fun NameTicket(number: Int, ticket: Ticket, onTicketChange: (Ticket) -> Unit) { //TODO cambiare textfield
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 5.dp, 10.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            TextField(
                value = ticket.nome,
                onValueChange = { onTicketChange(ticket.copy(nome = it)) },
                placeholder = { Text("Nome") },
                singleLine = true,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 2.5.dp)
            )
            TextField(
                value = ticket.cognome,
                onValueChange = { onTicketChange(ticket.copy(cognome = it)) },
                placeholder = { Text("Cognome") },
                singleLine = true,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 2.5.dp)
            )
        }
        TextField(
            value = ticket.email,
            onValueChange = { onTicketChange(ticket.copy(email = it)) },
            placeholder = { Text("eMail") },
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
        ){
            TextField(
                value = ticket.dataNascita,
                onValueChange = { onTicketChange(ticket.copy(dataNascita = it)) },
                placeholder = { Text("Data di nascita") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 2.5.dp, end = 5.dp)
                    .weight(1f)
            )
            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(buttonGradientType1()),
                onClick = {},
            ){
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "Scegli data",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun prev3(){
    ShowCheckout(rememberNavController())
}