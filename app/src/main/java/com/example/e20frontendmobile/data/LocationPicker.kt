package com.example.e20frontendmobile.data

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.e20frontendmobile.composables.CustomTextField
import com.example.e20frontendmobile.composables.CustomizableSearchBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@Composable
fun LocationPickerScreen() {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION

    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var geocodedLocation by remember { mutableStateOf<GeoPoint?>(null) }

    val scope = rememberCoroutineScope()
    var mapViewRef by remember { mutableStateOf<MapView?>(null) }

    // Launcher permessi
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                Toast.makeText(context, "Permesso posizione negato", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(LOCATION_PERMISSION)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {

        // Barra di ricerca con tasto Cerca della tastiera
        CustomizableSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { query ->
                if (query.isNotEmpty()) {
                    // ricerca Nominatim
                    scope.launch(Dispatchers.IO) {
                        try {
                            val url = URL(
                                "https://nominatim.openstreetmap.org/search?format=json&q=${
                                    URLEncoder.encode(query, "UTF-8")
                                }"
                            )
                            val conn = url.openConnection() as HttpURLConnection
                            conn.requestMethod = "GET"
                            conn.setRequestProperty(
                                "User-Agent",
                                "MyComposeApp/1.0 (email@example.com)"
                            )

                            val result = conn.inputStream.bufferedReader().use { it.readText() }
                            val jsonArray = JSONArray(result)
                            if (jsonArray.length() > 0) {
                                val first = jsonArray.getJSONObject(0)
                                val lat = first.getDouble("lat")
                                val lon = first.getDouble("lon")
                                val geoPoint = GeoPoint(lat, lon)

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
                                    Toast.makeText(context, "Luogo non trovato", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Errore geocoding: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            },
            searchResults = emptyList(),  // lista risultati non usata
            onResultClick = {},           // non usato
            placeholder = { Text("Cerca luogo") },
            trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
        )

        // Box con altezza fissa per la mappa
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
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

        Spacer(modifier = Modifier.height(16.dp))
        Text("Latitudine: ${selectedLocation?.latitude ?: "-"}")
        Text("Longitudine: ${selectedLocation?.longitude ?: "-"}")
    }
}








