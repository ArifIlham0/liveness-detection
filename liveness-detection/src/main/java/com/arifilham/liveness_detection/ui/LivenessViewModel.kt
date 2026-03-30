package com.arifilham.liveness_detection.ui

import android.app.Application
import androidx.camera.view.PreviewView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import com.arifilham.liveness_detection.analyzers.FrameAnalyzer
import com.arifilham.liveness_detection.cameras.CameraController
import com.arifilham.liveness_detection.cores.LivenessProcessor
import com.arifilham.liveness_detection.models.LivenessResult
import com.arifilham.liveness_detection.utils.OpenCVUtils
import com.arifilham.liveness_detection.visions.MediaPipeFaceMesh
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LivenessViewModel(application: Application) : AndroidViewModel(application) {
    init {
        OpenCVUtils.initialize()
    }

    private val faceMesh = MediaPipeFaceMesh(application)
    private val processor = LivenessProcessor(faceMesh)
    private val _livenessResult = MutableStateFlow(
        LivenessResult(
            status = com.arifilham.liveness_detection.models.LivenessStatus.NO_FACE,
            message = "Wajah tidak terdeteksi, mohon arahkan kamera ke wajah"
        )
    )
    val livenessResult: StateFlow<LivenessResult> = _livenessResult
    private val analyzer = FrameAnalyzer(processor) { result ->
        _livenessResult.value = result
    }
    private var cameraController: CameraController? = null

    fun startCamera(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        if (cameraController == null) {
            cameraController = CameraController(
                context = getApplication(),
                lifecycleOwner = lifecycleOwner,
                analyzer = analyzer
            )
        }

        cameraController?.startCamera(previewView)
    }
}