package com.arifilham.liveness_detection.ui

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun LivenessCameraView(viewModel: LivenessViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val livenessState by viewModel.livenessResult.collectAsState()

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).apply {
                viewModel.startCamera(this, lifecycleOwner)
            }
        }
    )
    Text(
        text = "Blink: ${livenessState.blinkDetected}",
        modifier = Modifier.padding(16.dp)
    )
}