package com.example.e20frontendmobile.activities.qrScanner

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.e20frontendmobile.apiService.TicketService
import com.example.e20frontendmobile.model.Ticket
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeScannerWithBottomSheet() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var scannedCode by remember { mutableStateOf<String?>(null) }
    var isValid by remember { mutableStateOf<Boolean?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    var ticket by remember { mutableStateOf<Ticket?>(null) }

    // Stato per permesso fotocamera
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val coroutine = rememberCoroutineScope()

    // Mostra BottomSheet solo se c'è un codice scansionato
    if (showSheet && scannedCode != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                scannedCode = null
                isValid = null
            },
            sheetState = sheetState
        ) {
            BottomSheetContent(
                scannedCode = scannedCode,
                isValid = isValid,
                onCopy = {
                    scannedCode?.let {
                        clipboardManager.setPrimaryClip(ClipData.newPlainText("QR Code", it))
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                    }
                },
                onShare = {
                    scannedCode?.let {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, it)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share QR Code"))
                    }
                },
                onClose = {
                    showSheet = false
                    scannedCode = null
                    isValid = null
                },
                ticket = ticket,
                context = context
            )
        }
    }

    if (hasCameraPermission) {
        AndroidView(factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }
                val analyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build().also { analysis ->
                        analysis.setAnalyzer(
                            ContextCompat.getMainExecutor(ctx),
                            QRCodeAnalyzer { qrCode ->
                                if (!showSheet) {
                                    scannedCode = qrCode
                                    coroutine.launch {
                                        ticket = TicketService(context).getById(qrCode)
                                    }
                                    showSheet = true
                                }
                            }
                        )
                    }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analyzer
                )
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        }, modifier = Modifier.fillMaxSize())
    } else {
        // Messaggio se permesso negato
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Permesso fotocamera necessario per usare lo scanner")
        }
    }
}


fun validateTicket(context: Context, ticket: String): Boolean{
    return TicketService(context).validate(ticket)
}

@Composable
fun BottomSheetContent(
    scannedCode: String?,
    isValid: Boolean?,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onClose: () -> Unit,
    context: Context,
    ticket: Ticket?
) {
    var checkValid by remember { mutableStateOf(isValid)}
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Biglietto", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))


        when (checkValid) {
            true -> Text("Biglietto valido ✅", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            false -> Text("Biglietto non valido ❌", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            null -> {
                if (ticket?.eValido == true) {
                    Text(
                        """
                ${ticket?.nome} ${ticket?.cognome}
                ${ticket?.email}
                ${ticket?.data_nascita}
            """.trimIndent()
                    )
                } else if (ticket?.eValido == null) {
                    CircularProgressIndicator()
                }
                else {
                    Text(
                        "Biglietto non valido ❌",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (ticket?.eValido == true && checkValid == null){
            OutlinedButton(onClick = {
                checkValid = validateTicket(context, scannedCode!!)
            }) {
                Text("Valida")
            }
        }
        else {
            OutlinedButton(onClick = onClose) {
                Text("Chiudi")
            }
        }
    }
}
