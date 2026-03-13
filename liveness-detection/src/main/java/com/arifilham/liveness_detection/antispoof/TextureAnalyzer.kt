package com.arifilham.liveness_detection.antispoof

import android.graphics.Bitmap
import com.arifilham.liveness_detection.utils.ImageUtils

class TextureAnalyzer {
    fun analyze(bitmap: Bitmap): Float {
        val variance = ImageUtils.laplacianVariance(bitmap)

        return variance
    }
}