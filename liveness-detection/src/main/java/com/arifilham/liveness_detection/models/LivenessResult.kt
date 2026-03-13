package com.arifilham.liveness_detection.models

data class LivenessResult(
    val blinkDetected: Boolean,
    val pose: FacePose?,
    val motionDetected: Boolean
)