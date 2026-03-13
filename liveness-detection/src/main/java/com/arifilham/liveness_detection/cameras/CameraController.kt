package com.arifilham.liveness_detection.cameras

import android.content.Context
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.arifilham.liveness_detection.analyzers.FrameAnalyzer
import java.util.concurrent.Executors

class CameraController(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val analyzer: FrameAnalyzer
) {
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }
            val imageAnalyzer = ImageAnalysis.Builder().setBackpressureStrategy(
                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            ).build().also {
                it.setAnalyzer(cameraExecutor, analyzer)
            }
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer,
            )
        }, ContextCompat.getMainExecutor(context))
    }
}