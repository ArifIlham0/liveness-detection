package com.arifilham.liveness_detection.cores

import android.graphics.Bitmap
import com.arifilham.liveness_detection.features.*
import com.arifilham.liveness_detection.models.LivenessResult
import com.arifilham.liveness_detection.utils.LandmarkUtils
import com.arifilham.liveness_detection.visions.MediaPipeFaceMesh

class LivenessProcessor(private val faceMesh: MediaPipeFaceMesh) {
    private val blinkDetector = BlinkDetector()
    private val poseEstimator = HeadPoseEstimator()
    private val motionDetector = MotionDetector()

    fun processFrame(bitmap: Bitmap): LivenessResult {
        val result = faceMesh.detect(bitmap)
        val landmarks = LandmarkUtils.extract(result)
        if (landmarks.isEmpty()) {
            return LivenessResult(
                blinkDetected = false,
                pose = null,
                motionDetected = false
            )
        }
        val ear = EyeAspectRatio.calculate(LandmarkUtils.leftEye(landmarks))
        val blink = blinkDetector.detect(ear)
        val pose = poseEstimator.estimate(landmarks)
        val motion = motionDetector.detect(bitmap)

        return LivenessResult(
            blinkDetected = blink,
            pose = pose,
            motionDetected = motion
        )
    }
}