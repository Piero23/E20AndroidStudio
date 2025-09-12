package com.example.e20frontendmobile.activities.evento

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.CalendarContract
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clipToBounds
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.e20frontendmobile.R
import com.example.e20frontendmobile.data.apiService.EventoLocation.EventService
import com.example.e20frontendmobile.data.apiService.Utente.UtenteService
import com.example.e20frontendmobile.model.Event
import com.example.e20frontendmobile.viewModels.EventViewModel
import io.ktor.http.content.LastModifiedVersion
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.time.ExperimentalTime


@Composable
fun ShowEvent(navController: NavHostController, isAdmin: Boolean, eventViewModel: EventViewModel) {

    var toggledBell by rememberSaveable { mutableStateOf(false) }
    var toggledHeart by rememberSaveable { mutableStateOf(false) }

    var calendarEventId by remember { mutableStateOf<Long?>(null) }

    val context = LocalContext.current

    val event = eventViewModel.selectedEvent

    if (event == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Seleziona un evento")
        }
        return
    }

    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }



    LaunchedEffect(event.id) {
        val service = EventService(context)
        imageBitmap = service.getImage(event.id)
        eventViewModel.spotsLeft(context)
        eventViewModel.getLocationFromEvent(context)
        toggledHeart = eventViewModel.checkIfPreferito(context)
    }

    var hasCalendarPermission by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasCalendarPermission = permissions[Manifest.permission.READ_CALENDAR] == true &&
                permissions[Manifest.permission.WRITE_CALENDAR] == true
    }

    LaunchedEffect(Unit) {
        val readGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
        val writeGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
        if (!readGranted || !writeGranted) {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR))
        } else {
            hasCalendarPermission = true
        }
    }

    LaunchedEffect(hasCalendarPermission) {
        if (hasCalendarPermission) {
            val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val savedId = prefs.getLong("event_id", -1L)
            if (savedId != -1L && isEventInCalendar(context, savedId)) {
                calendarEventId = savedId
                toggledBell = true
            }
        }
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
                Row{
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Caricamento immagine")
                }
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
                    if (isAdmin && eventViewModel.checkEventManager(context)) {
                        IconButton(onClick = {
                            navController.navigate("edit")
                        }) {
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
                        IconButton(onClick = {
                            toggledBell = !toggledBell
                            if (toggledBell) {
                                calendarEventId = addEventToCalendar(context, event.title, event.date);
                                saveEventId(context, calendarEventId ?: -1)
                                calendarEventId?.let {
                                    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit().putLong("event_id", it).apply()
                                }
                                Toast.makeText(context, "Evento aggiunto al calendario", Toast.LENGTH_SHORT).show()
                            } else {
                                calendarEventId?.let {
                                    removeEventFromCalendar(context, it)
                                    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit().remove("event_id").apply()
                                }
                                calendarEventId = null
                                Toast.makeText(context, "Evento rimosso dal calendario", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(
                                Icons.Filled.Notifications,
                                contentDescription = "Ricordamelo",
                                modifier = Modifier.size(50.dp),
                                tint = if (toggledBell) Color.Yellow else Color.Black
                            )
                        }
                        IconButton(onClick = {
                            if (UtenteService(context).getUtenteSub()!="no"){
                                toggledHeart = !toggledHeart
                                if (toggledHeart) {
                                    eventViewModel.salvaPreferito(context)
                                    Toast.makeText(context, "Evento aggiunto ai preferiti", Toast.LENGTH_SHORT).show()
                                } else {
                                    eventViewModel.removePreferiti(context)
                                    Toast.makeText(context, "Evento rimosso dai preferiti", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                Toast.makeText(context, "Devi essere registrato", Toast.LENGTH_SHORT).show()
                            }
                        }) {
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
                if (eventViewModel.spotsLeft==-1) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .alignBy(LastBaseline),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        eventViewModel.spotsLeft.toString(),
                        modifier = Modifier.alignBy(LastBaseline),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(106, 51, 0, 255)
                    )
                }
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
                                modifier = Modifier.alignBy(LastBaseline),
                                color = Color(106, 51, 0, 255),
                                text = event.prezzo.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                fontSize = 40.sp
                            )
                            Text(
                                "€",
                                modifier = Modifier.alignBy(LastBaseline),
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
        if (eventViewModel.selectedEventLocation==null){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row{
                    CircularProgressIndicator()
                    Text("Caricamento")
                }
            }
        }
        else{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    eventViewModel.selectedEventLocation!!.nome?.let { Text(it, fontSize = 16.sp) }
                    eventViewModel.selectedLocationAddress?.road?.let { Text(it, fontSize = 16.sp) }
                }
                Column(horizontalAlignment = Alignment.End) {
                    eventViewModel.selectedLocationAddress?.village?.let { Text(it, fontSize = 16.sp) }
                    eventViewModel.selectedLocationAddress?.postcode?.let { Text(it, fontSize = 16.sp) }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(15.dp, 10.dp, 15.dp, 50.dp)
                .clipToBounds()
        ) {
            MapScreen(45.0755969, 7.638332)
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
fun MapScreen(latitude: Double, longitude: Double) {
    var mapReady by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!mapReady) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row{
                    CircularProgressIndicator()
                    Text("Caricamento mappa")
                }
            }
        }

        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    Configuration.getInstance().load(
                        ctx,
                        ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
                    )
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)
                    controller.setCenter(GeoPoint(latitude, longitude))

                    // Marker fisso
                    Marker(this).apply {
                        position = GeoPoint(latitude, longitude)
                        title = "Posizione"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        overlays.add(this) // <-- qui
                    }

                    mapReady = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}


@OptIn(ExperimentalTime::class)
fun addEventToCalendar(context: Context, title: String, dateTime: LocalDateTime, durationMinutes: Long = 60): Long? {
    val startMillis = dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    val endMillis = startMillis + durationMinutes * 60 * 1000

    val values = ContentValues().apply {
        put(CalendarContract.Events.DTSTART, startMillis)
        put(CalendarContract.Events.DTEND, endMillis)
        put(CalendarContract.Events.TITLE, title)
        put(CalendarContract.Events.CALENDAR_ID, 1)
        put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.currentSystemDefault().id)
    }

    return if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
        context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)?.lastPathSegment?.toLong()
    } else null
}
fun removeEventFromCalendar(context: Context, eventId: Long) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
        val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        context.contentResolver.delete(uri, null, null)
    }
}

fun isEventInCalendar(context: Context, eventId: Long): Boolean {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
        return false
    }

    val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
    val cursor = context.contentResolver.query(uri, arrayOf(CalendarContract.Events._ID), null, null, null)
    val exists = cursor?.moveToFirst() == true
    cursor?.close()
    return exists
}

fun saveEventId(context: Context, eventId: Long) {
    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .edit()
        .putLong("event_id", eventId)
        .apply()
}
