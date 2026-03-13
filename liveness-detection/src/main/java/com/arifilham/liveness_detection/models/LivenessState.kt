package com.arifilham.liveness_detection.models

data class LivenessState(
    val blink: Boolean = false,
    val headMove: Boolean = false,
    val motion: Boolean = false,
    val textureOk: Boolean = false
)