package com.arifilham.liveness_detection.utils

import android.graphics.PointF
import com.arifilham.liveness_detection.visions.FaceLandmarkIndex
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult

object LandmarkUtils {
    fun extract(result: FaceLandmarkerResult): List<PointF> {
        val landmarks = mutableListOf<PointF>()
        val faceLandmarks = result.faceLandmarks().firstOrNull()
            ?: return emptyList()

        for (landmark in faceLandmarks) {
            landmarks.add(
                PointF(
                    landmark.x(),
                    landmark.y()
                )
            )
        }

        return landmarks
    }

    fun leftEye(landmarks: List<PointF>): List<PointF> {
        return FaceLandmarkIndex.LEFT_EYE.mapNotNull { index ->
            landmarks.getOrNull(index)
        }
    }

    fun rightEye(landmarks: List<PointF>): List<PointF> {
        return FaceLandmarkIndex.RIGHT_EYE.map {
            landmarks[it]
        }
    }
}