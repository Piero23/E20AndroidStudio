package com.example.e20frontendmobile.data

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.e20frontendmobile.composables.CustomTextField
import com.example.e20frontendmobile.composables.CustomizableSearchBar
import com.example.e20frontendmobile.data.apiService.EventoLocation.LocationService
import com.example.e20frontendmobile.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@Composable
fun LocationPickerPopup(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION

    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var geocodedLocation by remember { mutableStateOf<GeoPoint?>(null) }

    var nome by remember { mutableStateOf("") }
    var descrizione by remember { mutableStateOf("") }
    var alChiuso by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    var mapViewRef by remember { mutableStateOf<MapView?>(null) }

    // Launcher permessi
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) Toast.makeText(context, "Permesso posizione negato", Toast.LENGTH_SHORT).show()
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(LOCATION_PERMISSION)
        }
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Nuova Location", style = MaterialTheme.typography.titleLarge)

                CustomTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    placeholder = "Nome",
                    singleLine = true
                )

                CustomTextField(
                    value = descrizione,
                    onValueChange = { descrizione = it },
                    placeholder = "Descrizione",
                    singleLine = true
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Al chiuso")
                    Switch(alChiuso, { alChiuso = it })
                }

                // Barra di ricerca
                CustomizableSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { query ->
                        if (query.isNotEmpty()) {
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val url = URL(
                                        "https://nominatim.openstreetmap.org/search?format=json&q=${
                                            URLEncoder.encode(query, "UTF-8")
                                        }"
                                    )
                                    val conn = url.openConnection() as HttpURLConnection
                                    conn.requestMethod = "GET"
                                    conn.setRequestProperty("User-Agent", "MyComposeApp/1.0 (email@example.com)")

                                    val result = conn.inputStream.bufferedReader().use { it.readText() }
                                    val jsonArray = JSONArray(result)
                                    if (jsonArray.length() > 0) {
                                        val first = jsonArray.getJSONObject(0)
                                        val geoPoint = GeoPoint(first.getDouble("lat"), first.getDouble("lon"))

                                        withContext(Dispatchers.Main) {
                                            geocodedLocation = geoPoint
                                            selectedLocation = geoPoint

                                            mapViewRef?.let { map ->
                                                map.overlays.removeAll { it is Marker }
                                                Marker(map).apply {
                                                    position = geoPoint
                                                    title = "Selezionato"
                                                    map.overlays.add(this)
                                                }
                                                map.controller.animateTo(geoPoint)
                                                map.invalidate()
                                            }
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "Luogo non trovato", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Errore geocoding: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    },
                    searchResults = emptyList(),
                    onResultClick = {},
                    placeholder = { Text("Cerca luogo") },
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                )

                // MAPPA FISSA
                Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clipToBounds()
        ) {
            AndroidView(
                factory = { ctx ->
                    MapView(ctx).apply {
                        mapViewRef = this
                        org.osmdroid.config.Configuration.getInstance()
                            .load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
                        setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(10.0)
                        controller.setCenter(GeoPoint(45.4641, 9.1919))

                        // Overlay per tap sulla mappa
                        val touchOverlay = object : org.osmdroid.views.overlay.Overlay() {
                            override fun onSingleTapConfirmed(
                                e: MotionEvent?,
                                mapView: MapView?
                            ): Boolean {
                                e ?: return false
                                mapView ?: return false
                                val geoPoint =
                                    mapView.projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                                selectedLocation = geoPoint

                                mapView.overlays.removeAll { it is Marker }
                                Marker(mapView).apply {
                                    position = geoPoint
                                    title = "Selezionato"
                                    mapView.overlays.add(this)
                                }
                                mapView.invalidate()
                                return true
                            }

                            override fun draw(
                                canvas: Canvas?,
                                mapView: MapView?,
                                shadow: Boolean
                            ) {}
                        }

                        overlays.add(touchOverlay)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        when {
                            nome.isBlank() -> {
                                Toast.makeText(context, "Inserisci il nome della location", Toast.LENGTH_SHORT).show()
                            }
                            descrizione.isBlank() -> {
                                Toast.makeText(context, "Inserisci la descrizione della location", Toast.LENGTH_SHORT).show()
                            }
                            selectedLocation == null -> {
                                Toast.makeText(context, "Seleziona una posizione sulla mappa", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                scope.launch {
                                    try {
                                        LocationService(context).create(
                                            Location(
                                                nome = nome,
                                                descrizione = descrizione,
                                                chiuso = alChiuso,
                                                position = "${selectedLocation!!.latitude},${selectedLocation!!.longitude}"
                                            )
                                        )
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "Location salvata!", Toast.LENGTH_SHORT).show()
                                            onDismiss()
                                        }
                                    } catch (e: Exception) {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "Errore salvataggio: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Salva")
                }

            }
        }
    }
}











