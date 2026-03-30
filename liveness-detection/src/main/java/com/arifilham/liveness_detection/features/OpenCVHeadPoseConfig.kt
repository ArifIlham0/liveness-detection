package com.arifilham.liveness_detection.features

object OpenCVHeadPoseConfig {
    const val DEFAULT_FOCAL_LENGTH = 1000f
    const val DEFAULT_CENTER_X = 320f
    const val DEFAULT_CENTER_Y = 240f
    const val YAW_THRESHOLD = 0.3f
    const val PITCH_THRESHOLD = 0.2f
    const val ROLL_THRESHOLD = 0.15f

    fun radianToDegrees(radian: Float): Float = radian * 57.2958f
    fun degreesToRadian(degrees: Float): Float = degrees / 57.2958f
}