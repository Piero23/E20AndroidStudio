package com.example.e20frontendmobile.activities.user

import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e20frontendmobile.model.Ordine
import com.example.e20frontendmobile.model.Ticket
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme
import com.example.e20frontendmobile.viewModels.OrdineViewModel


//TODO PULIRE TUTTO (controllo errori e qrCode cliccabile)
@Composable
fun OrdersList(ordineViewModel: OrdineViewModel = viewModel()) {
    val ordini = ordineViewModel.ordiniUtente

    if (ordini?.isEmpty() ?: true) {
        Text("Nessun ordine")
    } else {
        LazyColumn {
            items(ordini) { ordine ->
                ExpandableCard(ordine)
            }
        }
    }
}


@Composable
fun Orders(ordineViewModel: OrdineViewModel = viewModel ()) {

    val context = LocalContext.current
    LaunchedEffect(true) {
        ordineViewModel.getOrdini(context)
    }


    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.padding(vertical = 30.dp))
        Text(
            "I Tuoi Ordini",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Divider()
        OrdersList(ordineViewModel)
    }
}

@Composable
fun ExpandableCard(ordine: Ordine) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(3.dp, Color(0xFFFF9800)) // bordo arancione come nell'immagine
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "evento ACCAZZo",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                ordine.biglietti.forEach { participant ->
                    Divider()
                    ParticipantRow(participant)
                }
            }
        }
    }
}

@Composable
fun ParticipantRow(participant: Ticket) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                participant.nome?.let { Text(it, fontWeight = FontWeight.Medium) }
                participant.email?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                participant.dataNascita?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (participant.eValido == true) Color.Green else Color.Red)
            )
        }
    }
}

data class EventItem(
    val title: String,
    val participants: List<Participant>
)

data class Participant(
    val name: String,
    val email: String,
    val date: String,
    val status: Boolean // true = verde, false = rosso
)

@Composable
@Preview
fun OrdersPreview(){
    E20FrontendMobileTheme {
        Orders()
    }
}
