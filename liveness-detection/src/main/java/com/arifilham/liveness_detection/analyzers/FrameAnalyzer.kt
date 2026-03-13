package com.arifilham.liveness_detection.analyzers

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.arifilham.liveness_detection.cores.LivenessProcessor
import com.arifilham.liveness_detection.models.LivenessResult

class FrameAnalyzer(
    private val processor: LivenessProcessor,
    private val onResult: (LivenessResult) -> Unit
) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val bitmap = FrameConverter.imageProxyToBitmap(imageProxy)
        val result = processor.processFrame(bitmap)

        onResult(result)
        imageProxy.close()
    }
}