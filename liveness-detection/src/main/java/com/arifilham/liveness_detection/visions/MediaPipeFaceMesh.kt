package com.arifilham.liveness_detection.visions

import android.content.Context
import android.graphics.Bitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.*

class MediaPipeFaceMesh(context: Context) {
    private val faceLandmarker: FaceLandmarker

    init {
        val baseOptions = BaseOptions.builder().setModelAssetPath("face_landmarker.task").build()
        val options = FaceLandmarker.FaceLandmarkerOptions.builder()
            .setBaseOptions(baseOptions)
            .setNumFaces(1)
            .setRunningMode(RunningMode.IMAGE)
            .build()

        faceLandmarker = FaceLandmarker.createFromOptions(context, options)
    }

    fun detect(bitmap: Bitmap): FaceLandmarkerResult {
        val mpImage = BitmapImageBuilder(bitmap).build()

        return faceLandmarker.detect(mpImage)
    }
}