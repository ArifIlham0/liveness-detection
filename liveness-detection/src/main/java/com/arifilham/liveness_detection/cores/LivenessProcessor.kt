package com.arifilham.liveness_detection.cores

import android.graphics.Bitmap
import com.arifilham.liveness_detection.features.*
import com.arifilham.liveness_detection.models.LivenessResult
import com.arifilham.liveness_detection.models.LivenessStatus
import com.arifilham.liveness_detection.utils.LandmarkUtils
import com.arifilham.liveness_detection.visions.MediaPipeFaceMesh

class LivenessProcessor(private val faceMesh: MediaPipeFaceMesh) {
    private val blinkDetector = BlinkDetector()
    private val poseEstimator = HeadPoseEstimator()

    private var blinked = false
    private var turnedLeft = false
    private var turnedRight = false
    private var lastTurnStatus: LivenessStatus? = null
    private var lastTurnTimestamp: Long = 0L

    private val TURN_HOLD_MS = 1000L

    fun processFrame(bitmap: Bitmap): LivenessResult {
        val result = faceMesh.detect(bitmap)
        val landmarks = LandmarkUtils.extract(result)
        if (landmarks.isEmpty()) {
            lastTurnStatus = null
            lastTurnTimestamp = 0L
            return LivenessResult(LivenessStatus.NO_FACE, "Wajah tidak terdeteksi, mohon arahkan kamera ke wajah")
        }

        val ear = EyeAspectRatio.calculate(LandmarkUtils.leftEye(landmarks))
        val blink = blinkDetector.detect(ear)
        val pose = poseEstimator.estimate(landmarks)

        if (blink && !blinked) blinked = true

        var turnStatus: LivenessStatus? = null
        if (pose.yaw > 0.35f) {
            if (!turnedRight) turnedRight = true
            turnStatus = LivenessStatus.TURN_RIGHT
        } else if (pose.yaw < -0.35f) {
            if (!turnedLeft) turnedLeft = true
            turnStatus = LivenessStatus.TURN_LEFT
        }

        val now = System.currentTimeMillis()
        if (turnStatus != null) {
            lastTurnStatus = turnStatus
            lastTurnTimestamp = now
            return LivenessResult(
                status = turnStatus,
                message = if (turnStatus == LivenessStatus.TURN_RIGHT) "Anda sedang menoleh ke Kanan" else "Anda sedang menoleh ke Kiri"
            )
        } else if (lastTurnStatus != null && now - lastTurnTimestamp < TURN_HOLD_MS) {
            return LivenessResult(
                status = lastTurnStatus!!,
                message = if (lastTurnStatus == LivenessStatus.TURN_RIGHT) "Anda sedang menoleh ke Kanan" else "Anda sedang menoleh ke Kiri"
            )
        } else {
            lastTurnStatus = null
        }

        if (blinked && turnedLeft && turnedRight) {
            return LivenessResult(LivenessStatus.COMPLETE, "Liveness check selesai!")
        }

        if (blink && !turnedLeft) {
            return LivenessResult(LivenessStatus.BLINK, "Berkedip Terdeteksi!")
        }

        return LivenessResult(LivenessStatus.GOOD, "Posisi Bagus, Tetap Diam...")
    }
}