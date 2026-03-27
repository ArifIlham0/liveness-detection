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
        val eyeCenterX = (leftEye.x + rightEye.x) / 2f
        val eyeCenterY = (leftEye.y + rightEye.y) / 2f
        val eyeDist = rightEye.x - leftEye.x
        val yaw = (nose.x - eyeCenterX) / eyeDist
        val pitch = nose.y - eyeCenterY
        val roll = atan2(rightEye.y - leftEye.y, rightEye.x - leftEye.x)

        return FacePose(yaw, pitch, roll)
    }
}