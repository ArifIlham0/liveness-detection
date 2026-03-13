package com.arifilham.liveness_detection.features

import android.graphics.PointF
import kotlin.math.hypot

object EyeAspectRatio {
    fun calculate(points: List<PointF>): Float {
        val A = distance(points[1], points[5])
        val B = distance(points[2], points[4])
        val C = distance(points[0], points[3])

        return (A + B) / (2.0f * C)
    }

    private fun distance(p1: PointF, p2: PointF): Float {
        return hypot(p1.x - p2.x, p1.y - p2.y)
    }
}
