package com.arifilham.liveness_detection.models

data class LivenessResult(
    val status: LivenessStatus,
    val message: String? = null
)