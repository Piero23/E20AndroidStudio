package com.example.e20frontendmobile.activities.user

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme


@Composable
fun Orders() {
    val items = listOf(
        EventItem(
            title = "Serata Shots - 5/11",
            participants = listOf()
        ),
        EventItem(
            title = "Serata Filo Rosso - 10/7",
            participants = listOf(
                Participant("Mario Rossi", "mariorossi@pisellopalle.com", "9/11/2003", false),
                Participant("Paolo Bonolis", "baoloponolis@pisellopalle.com", "9/11/2003", true),
            )
        )
    )

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
        items.forEach { event ->
            ExpandableCard(event)
        }
    }
}

@Composable
fun ExpandableCard(event: EventItem) {
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
                    text = event.title,
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
                event.participants.forEach { participant ->
                    Divider()
                    ParticipantRow(participant)
                }
            }
        }
    }
}

@Composable
fun ParticipantRow(participant: Participant) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(participant.name, fontWeight = FontWeight.Medium)
                Text(participant.email, style = MaterialTheme.typography.bodySmall)
                Text(participant.date, style = MaterialTheme.typography.bodySmall)
            }
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (participant.status) Color.Green else Color.Red)
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
