package com.arifilham.liveness_detection.utils

import android.graphics.Bitmap
import kotlin.math.pow
import androidx.core.graphics.get

object ImageUtils {
    fun laplacianVariance(bitmap: Bitmap): Float {
        val width = bitmap.width
        val height = bitmap.height
        val gray = IntArray(width * height)
        var index = 0

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)
                val r = (pixel shr 16) and 0xff
                val g = (pixel shr 8) and 0xff
                val b = pixel and 0xff
                val grayValue = (0.299 * r + 0.587 * g + 0.114 * b).toInt()

                gray[index++] = grayValue
            }
        }

        val laplacianValues = mutableListOf<Float>()

        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                val center = gray[y * width + x]
                val laplacian = gray[(y - 1) * width + x] +
                        gray[(y + 1) * width + x] +
                        gray[y * width + x - 1] +
                        gray[y * width + x + 1] -
                        4 * center

                laplacianValues.add(laplacian.toFloat())
            }
        }

        val mean = laplacianValues.average()
        val variance = laplacianValues.map { (it - mean).pow(2) }.average()

        return variance.toFloat()
    }

    fun frameDifference(prev: Bitmap, current: Bitmap): Float {
        val width = prev.width
        val height = prev.height
        var diffSum = 0f

        for (y in 0 until height step 10) {
            for (x in 0 until width step 10) {
                val p1 = prev[x, y]
                val p2 = current[x, y]
                val r1 = (p1 shr 16) and 0xff
                val g1 = (p1 shr 8) and 0xff
                val b1 = p1 and 0xff
                val r2 = (p2 shr 16) and 0xff
                val g2 = (p2 shr 8) and 0xff
                val b2 = p2 and 0xff
                val diff = kotlin.math.abs(r1 - r2) +
                        kotlin.math.abs(g1 - g2) +
                        kotlin.math.abs(b1 - b2)

                diffSum += diff
            }
        }

        return diffSum
    }
}