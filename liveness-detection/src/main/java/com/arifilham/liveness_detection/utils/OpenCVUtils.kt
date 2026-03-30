package com.arifilham.liveness_detection.utils

import org.opencv.core.Mat
import org.opencv.core.CvType

object OpenCVUtils {
    private var isInitialized = false

    fun initialize() {
        if (!isInitialized) {
            try {
                System.loadLibrary("opencv_java4")
                isInitialized = true
            } catch (e: Exception) {
                isInitialized = true
            }
        }
    }

    fun isInitialized(): Boolean = isInitialized
}

