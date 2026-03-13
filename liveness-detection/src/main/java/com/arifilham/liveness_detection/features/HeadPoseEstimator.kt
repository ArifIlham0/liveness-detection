package com.arifilham.liveness_detection.features

import android.graphics.PointF
import com.arifilham.liveness_detection.models.FacePose
import com.arifilham.liveness_detection.visions.FaceLandmarkIndex
import kotlin.math.atan2

class HeadPoseEstimator {
    fun estimate(landmarks: List<PointF>): FacePose {
        val leftEye = landmarks[FaceLandmarkIndex.LEFT_EYE[0]]
        val rightEye = landmarks[FaceLandmarkIndex.RIGHT_EYE[0]]
        val nose = landmarks[FaceLandmarkIndex.NOSE_TIP]
        val dx = rightEye.x - leftEye.x
        val dy = rightEye.y - leftEye.y
        val yaw = atan2(dy, dx)
        val pitch = nose.y - (leftEye.y + rightEye.y) / 2
        val roll = atan2(dy, dx)

        return FacePose(yaw, pitch, roll)
    }
}