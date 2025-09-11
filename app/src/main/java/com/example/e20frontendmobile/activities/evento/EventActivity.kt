package com.example.e20frontendmobile.activities.evento

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.e20frontendmobile.R
import com.example.e20frontendmobile.data.apiService.EventoLocation.EventService
import com.example.e20frontendmobile.viewModels.EventViewModel
import kotlinx.coroutines.delay


@Composable
fun ShowEvent(navController: NavHostController, isAdmin: Boolean, eventViewModel: EventViewModel) {

    var toggledBell by rememberSaveable { mutableStateOf(false) }
    var toggledHeart by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    val event = eventViewModel.selectedEvent

    if (event == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Seleziona un evento")
        }
        return
    }

    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var spotsLeft: Int by remember { mutableStateOf(0) }


    LaunchedEffect(event.id) {
        val service = EventService(context)
        imageBitmap = service.getImage(event.id)
        spotsLeft = service.spotsLeft(event.id)
    }

    Column(modifier = Modifier.verticalScroll(
        enabled = true,
        state = ScrollState(0)
    )) {
        // Immagine
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!.asImageBitmap(),
                contentDescription = "Event Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Caricamento in corso...")
            }
        }

        Column(modifier = Modifier.padding(15.dp, 10.dp, 15.dp, 0.dp)) {
            // Titolo e info evento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        event.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Row {
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(255, 157, 71, 255),
                                            Color(199, 79, 0, 255)
                                        )
                                    )
                                )
                                .height(40.dp)
                                .wrapContentWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            TextWithShadow(
                                modifier = Modifier.padding(horizontal=10.dp),
                                text = "${event.date.month.name.take(3).capitalize()} ${event.date.dayOfMonth}, ${event.date.year} - ${event.date.hour}.${event.date.minute}"
                            )
                        }

                        if (event.restricted) {
                            Spacer(modifier = Modifier.size(10.dp))
                            // TODO icona restricted
                        }
                    }
                }

                Column(Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp)) {
                    if (isAdmin) {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                Icons.Filled.Create,
                                contentDescription = "Modifica",
                                modifier = Modifier.size(50.dp),
                                tint = Color.Black
                            )
                        }
                        IconButton(onClick = { navController.navigate("scanner") }) {
                            Icon(
                                Icons.Filled.QrCode,
                                contentDescription = "Scansiona",
                                modifier = Modifier.size(50.dp),
                                tint = Color.Black
                            )
                        }
                    } else {
                        IconButton(onClick = { toggledBell = !toggledBell }) {
                            Icon(
                                Icons.Filled.Notifications,
                                contentDescription = "Ricordamelo",
                                modifier = Modifier.size(50.dp),
                                tint = if (toggledBell) Color.Yellow else Color.Black
                            )
                        }
                        IconButton(onClick = { toggledHeart = !toggledHeart }) {
                            Icon(
                                Icons.Filled.FavoriteBorder,
                                contentDescription = "Salva",
                                modifier = Modifier.size(50.dp),
                                tint = if (toggledHeart) Color.Red else Color.Black
                            )
                        }
                    }
                }
            }

            // Descrizione
            Column(modifier = Modifier.padding(0.dp, 80.dp, 0.dp, 80.dp)) {
                Text(
                    "Descrizione",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = event.description,
                    modifier = Modifier.padding(2.dp, 5.dp, 0.dp, 0.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Posti disponibili
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.alignBy(LastBaseline),
                    text = "Posti disponibili: ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    spotsLeft.toString(),
                    modifier = Modifier.alignBy(LastBaseline),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(106, 51, 0, 255)
                )
            }

            // Ticket box
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Box(modifier = Modifier.width(280.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "Ticket",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.FillWidth
                    )
                    Column(modifier = Modifier.padding(80.dp, 20.dp, 0.dp, 0.dp)) {
                        Text(
                            "1 Biglietto",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(modifier = Modifier.offset(0.dp, (-8).dp)) {
                            Text(
                                event.prezzo.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                fontSize = 40.sp,
                                color = Color(106, 51, 0, 255)
                            )
                            Text(
                                "€",
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 20.sp,
                                color = Color(182, 97, 17, 255)
                            )
                        }
                    }
                }
            }

            // Compra ora
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 15.dp, 22.dp, 80.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navController.navigate("checkout") }) {
                    Text(
                        "Compra Ora",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .graphicsLayer(alpha = 0.99f)
                            .drawWithCache {
                                val brush = Brush.horizontalGradient(
                                    listOf(Color(255, 157, 71, 255), Color(199, 79, 0, 255))
                                )
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(brush, blendMode = BlendMode.SrcAtop)
                                }
                            }
                    )
                }
            }
        }

        // Location
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                "Location",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Via di tua mamma", fontSize = 16.sp)
                Text("118", fontSize = 16.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Napoli (NA)", fontSize = 16.sp)
                Text("88888", fontSize = 16.sp)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
                .padding(15.dp, 10.dp, 15.dp, 50.dp)
        ) {
            //TODO
//            WebViewScreen(45.0755969, 7.638332)
        }
    }
}

@Composable
fun TextWithShadow(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        color = Color.DarkGray,
        modifier = modifier
            .offset(x = 1.dp, y = 1.dp)
            .alpha(0.75f)
    )
    Text(
        text = text,
        color = Color.White,
        modifier = modifier
    )
}

@Composable
fun WebViewScreen(latitude: Double, longitude: Double) {
    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
            }
        },
        update = { web ->
            web.loadUrl("https://maps.google.com/?q=${latitude},${longitude}")
        }
    )
}

@Composable
@Preview
fun prev() {
    // ShowEvent(rememberNavController(), true, EventViewModel(LocalContext.current))
}

@Composable
@Preview
fun prev2() {
    // ShowEvent(rememberNavController(), false, EventViewModel(LocalContext.current))
}