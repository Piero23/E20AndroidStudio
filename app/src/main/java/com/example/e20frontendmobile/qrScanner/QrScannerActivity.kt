package com.example.e20frontendmobile.qrScanner

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
import java.util.*

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
                }
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
                                    isValid = checkTicket(qrCode) // Valida il biglietto
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

// Funzione di validazione (esempio)
fun checkTicket(qrCode: String): Boolean {
    // Esempio: se il codice contiene "VALID" allora è valido
    return qrCode.contains("VALID")
}

@Composable
fun BottomSheetContent(
    scannedCode: String?,
    isValid: Boolean?,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Biglietto", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        when (isValid) {
            true -> Text("Biglietto valido ✅", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            false -> Text("Biglietto non valido ❌", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            null -> Text(scannedCode ?: "", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isValid == null) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onCopy) {
                    Icon(Icons.Default.Create, contentDescription = "Copy")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy")
                }
                Button(onClick = onShare) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedButton(onClick = onClose) {
            Text("Close")
        }
    }
}
