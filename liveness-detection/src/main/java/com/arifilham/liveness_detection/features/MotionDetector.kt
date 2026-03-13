package com.arifilham.liveness_detection.features

import android.graphics.Bitmap
import com.arifilham.liveness_detection.utils.ImageUtils

class MotionDetector {
    private var previousFrame: Bitmap? = null

    fun detect(frame: Bitmap): Boolean {
        val prev = previousFrame ?: run {
            previousFrame = frame
            return false
        }

        val diff = ImageUtils.frameDifference(prev, frame)

        previousFrame = frame

        return diff > 5
    }
}