package com.arifilham.livenessdetectionlibrary

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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
import com.arifilham.liveness_detection.models.LivenessStatus

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        }
        setContent {
            val viewModel: LivenessViewModel = viewModel()
            val livenessResult by viewModel.livenessResult.collectAsState()

            if (livenessResult.status == LivenessStatus.COMPLETE) {
                startActivity(Intent(this, SuccessActivity::class.java))
            }

            Box(modifier = Modifier.fillMaxSize()) {
                LivenessCameraView(viewModel = viewModel)

                Column(
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(top = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = livenessResult.message ?: "",
                        color = when (livenessResult.status) {
                            LivenessStatus.NO_FACE -> Color.Red
                            LivenessStatus.BLINK -> Color.Cyan
                            LivenessStatus.TURN_LEFT,
                            LivenessStatus.TURN_RIGHT -> Color.Green
                            LivenessStatus.GOOD -> Color.White
                            LivenessStatus.COMPLETE -> Color.Magenta
                        }
                    )
                }
            }
        }
    }
}