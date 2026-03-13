package com.arifilham.liveness_detection.features

class BlinkDetector {
    private val threshold = 0.21f
    private var frameCounter = 0

    fun detect(ear: Float): Boolean {
        if (ear < threshold) {
            frameCounter++
        } else {
            if (frameCounter >= 2) {
                frameCounter = 0
                return true
            }

            frameCounter = 0
        }

        return false
    }
}