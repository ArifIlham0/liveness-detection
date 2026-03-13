package com.arifilham.livenessdetectionlibrary

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arifilham.liveness_detection.ui.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        }
        setContent {
            val viewModel: LivenessViewModel = viewModel()
            val livenessResult by viewModel.livenessResult.collectAsState()

            Box(modifier = Modifier.fillMaxSize()) {
                LivenessCameraView(viewModel = viewModel)

                Column(
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(top = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (livenessResult.pose == null) {
                        Text(
                            text = "Wajah tidak terdeteksi, mohon arahkan kamera ke wajah",
                            color = Color.Red
                        )
                    } else {
                        val pose = livenessResult.pose!!
                        when {
                            pose.yaw > 20f -> Text("Anda sedang menoleh ke Kanan", color = Color.Green)
                            pose.yaw < -20f -> Text("Anda sedang menoleh ke Kiri", color = Color.Green)
                            livenessResult.blinkDetected -> Text("Berkedip Terdeteksi!", color = Color.Cyan)
                            else -> Text("Posisi Bagus, Tetap Diam...", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}