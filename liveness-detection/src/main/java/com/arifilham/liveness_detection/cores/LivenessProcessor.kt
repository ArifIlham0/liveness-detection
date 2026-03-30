package com.arifilham.liveness_detection.cores

import android.graphics.Bitmap
import com.arifilham.liveness_detection.features.*
import com.arifilham.liveness_detection.models.LivenessResult
import com.arifilham.liveness_detection.models.LivenessStatus
import com.arifilham.liveness_detection.utils.LandmarkUtils
import com.arifilham.liveness_detection.visions.MediaPipeFaceMesh

class LivenessProcessor(private val faceMesh: MediaPipeFaceMesh) {
    private val blinkDetector = BlinkDetector()
    private val poseEstimator = OpenCVHeadPoseEstimator()
    private val fallbackPoseEstimator = HeadPoseEstimator()

    private var blinked = false
    private var turnedLeft = false
    private var turnedRight = false
    private var lastTurnStatus: LivenessStatus? = null
    private var lastTurnTimestamp: Long = 0L

    private var lastPose: Float? = null
    private var lastPoseStatus: LivenessStatus? = null

    private val TURN_HOLD_MS = 1000L

    fun processFrame(bitmap: Bitmap): LivenessResult {
        val result = faceMesh.detect(bitmap)
        val landmarks = LandmarkUtils.extract(result)
        if (landmarks.isEmpty()) {
            if (lastPoseStatus == LivenessStatus.TURN_LEFT || lastPoseStatus == LivenessStatus.TURN_RIGHT) {
                return LivenessResult(LivenessStatus.NO_FACE, "Face is too tilted")
            }
            lastTurnStatus = null
            lastTurnTimestamp = 0L
            return LivenessResult(LivenessStatus.NO_FACE, "Face not detected")
        }

        val ear = EyeAspectRatio.calculate(LandmarkUtils.leftEye(landmarks))
        val blink = blinkDetector.detect(ear)

        val pose = try {
            poseEstimator.estimate(landmarks)
        } catch (_: Throwable) {
            fallbackPoseEstimator.estimate(landmarks)
        }

        if (blink && !blinked) blinked = true

        val YAW_THRESHOLD = 0.3f
        var turnStatus: LivenessStatus? = null
        if (pose.yaw > YAW_THRESHOLD) {
            if (!turnedRight) turnedRight = true
            turnStatus = LivenessStatus.TURN_RIGHT
        } else if (pose.yaw < -YAW_THRESHOLD) {
            if (!turnedLeft) turnedLeft = true
            turnStatus = LivenessStatus.TURN_LEFT
        }
        lastPose = pose.yaw
        lastPoseStatus = turnStatus

        val now = System.currentTimeMillis()
        if (turnStatus != null) {
            lastTurnStatus = turnStatus
            lastTurnTimestamp = now
            return LivenessResult(
                status = turnStatus,
                message = if (turnStatus == LivenessStatus.TURN_RIGHT) "You are looking to the right (yaw: %.2f)".format(pose.yaw) else "You are looking to the left (yaw: %.2f)".format(pose.yaw)
            )
        } else if (lastTurnStatus != null && now - lastTurnTimestamp < TURN_HOLD_MS) {
            return LivenessResult(
                status = lastTurnStatus!!,
                message = if (lastTurnStatus == LivenessStatus.TURN_RIGHT) "You are looking to the right (yaw: %.2f)".format(pose.yaw) else "You are looking to the left (yaw: %.2f)".format(pose.yaw)
            )
        } else {
            lastTurnStatus = null
        }

        if (blinked && turnedLeft && turnedRight) {
            return LivenessResult(LivenessStatus.COMPLETE, "Liveness check complete!")
        }

        if (blink && !turnedLeft) {
            return LivenessResult(LivenessStatus.BLINK, "Blinking Detected!")
        }

        return LivenessResult(LivenessStatus.GOOD, "Good Position, Stay Still... (yaw: %.2f)".format(pose.yaw))
    }
}