package com.arifilham.liveness_detection.antispoof

import kotlin.math.pow

class ScreenReflectionDetector {
    fun detect(brightnessSeries: List<Float>): Boolean {
        val variance = brightnessSeries.variance()

        return variance < 0.5
    }
}

fun List<Float>.variance(): Double {
    if (this.isEmpty()) return 0.0
    val mean = this.average()
    return this.map { (it - mean).pow(2) }.average()
}