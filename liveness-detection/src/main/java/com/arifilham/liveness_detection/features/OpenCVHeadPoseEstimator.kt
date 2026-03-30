package com.arifilham.liveness_detection.features

import android.graphics.PointF
import com.arifilham.liveness_detection.models.FacePose
import com.arifilham.liveness_detection.visions.FaceLandmarkIndex
import org.opencv.calib3d.Calib3d
import org.opencv.core.*
import kotlin.math.sqrt

class OpenCVHeadPoseEstimator {
    private val focalLength = 1000.0
    private val centerX = 320.0
    private val centerY = 240.0

    fun estimate(landmarks: List<PointF>): FacePose {
        try {
            val objectPoints = MatOfPoint3f()
            val imagePoints = MatOfPoint2f()

            val obj3DPoints = arrayOf(
                Point3(0.0, 0.0, 0.0),
                Point3(0.0, -30.0, -30.0),
                Point3(-30.0, 30.0, -30.0),
                Point3(30.0, 30.0, -30.0),
                Point3(-30.0, -30.0, -30.0),
                Point3(30.0, -30.0, -30.0)
            )
            objectPoints.fromArray(*obj3DPoints)

            val img2DPoints = arrayOf(
                Point(landmarks[FaceLandmarkIndex.NOSE_TIP].x.toDouble(),
                    landmarks[FaceLandmarkIndex.NOSE_TIP].y.toDouble()),
                Point(landmarks[8].x.toDouble(), landmarks[8].y.toDouble()),
                Point(landmarks[FaceLandmarkIndex.LEFT_EYE[0]].x.toDouble(),
                    landmarks[FaceLandmarkIndex.LEFT_EYE[0]].y.toDouble()),
                Point(landmarks[FaceLandmarkIndex.RIGHT_EYE[0]].x.toDouble(),
                    landmarks[FaceLandmarkIndex.RIGHT_EYE[0]].y.toDouble()),
                Point(landmarks[61].x.toDouble(), landmarks[61].y.toDouble()),
                Point(landmarks[291].x.toDouble(), landmarks[291].y.toDouble())
            )
            imagePoints.fromArray(*img2DPoints)

            val cameraMatrix = Mat(3, 3, CvType.CV_64F)
            cameraMatrix.put(0, 0, focalLength)
            cameraMatrix.put(0, 2, centerX)
            cameraMatrix.put(1, 1, focalLength)
            cameraMatrix.put(1, 2, centerY)
            cameraMatrix.put(2, 2, 1.0)

            val distCoeffs = MatOfDouble(0.0, 0.0, 0.0, 0.0, 0.0)

            val rvec = Mat()
            val tvec = Mat()

            Calib3d.solvePnP(
                objectPoints,
                imagePoints,
                cameraMatrix,
                distCoeffs,
                rvec,
                tvec,
                false,
                Calib3d.SOLVEPNP_ITERATIVE
            )

            val rotMat = Mat()
            Calib3d.Rodrigues(rvec, rotMat)

            val (yaw, pitch, roll) = rotationMatrixToEulerAngles(rotMat)

            cameraMatrix.release()
            rvec.release()
            tvec.release()
            rotMat.release()

            return FacePose(
                yaw = yaw,
                pitch = pitch,
                roll = roll
            )
        } catch (_: Exception) {
            return estimateSimple(landmarks)
        }
    }
    private fun rotationMatrixToEulerAngles(rotMat: Mat): Triple<Float, Float, Float> {
        try {
            val r00 = rotMat.get(0, 0)[0].toFloat()
            val r10 = rotMat.get(1, 0)[0].toFloat()
            val r20 = rotMat.get(2, 0)[0].toFloat()
            val r21 = rotMat.get(2, 1)[0].toFloat()
            val r22 = rotMat.get(2, 2)[0].toFloat()
            val yaw = Math.atan2(r10.toDouble(), r00.toDouble()).toFloat()
            val pitch = Math.atan2(-r20.toDouble(), sqrt((r21 * r21 + r22 * r22).toDouble())).toFloat()
            val roll = Math.atan2(r21.toDouble(), r22.toDouble()).toFloat()

            return Triple(yaw, pitch, roll)
        } catch (_: Exception) {
            return Triple(0f, 0f, 0f)
        }
    }
    fun estimateSimple(landmarks: List<PointF>): FacePose {
        val leftEye = landmarks[FaceLandmarkIndex.LEFT_EYE[0]]
        val rightEye = landmarks[FaceLandmarkIndex.RIGHT_EYE[0]]
        val nose = landmarks[FaceLandmarkIndex.NOSE_TIP]
        val eyeCenterX = (leftEye.x + rightEye.x) / 2f
        val eyeCenterY = (leftEye.y + rightEye.y) / 2f
        val eyeDist = rightEye.x - leftEye.x

        if (kotlin.math.abs(eyeDist) < 1.0f) {
            return FacePose(0f, 0f, 0f)
        }

        var yaw = (nose.x - eyeCenterX) / eyeDist
        yaw = yaw.coerceIn(-1f, 1f)
        val pitch = (nose.y - eyeCenterY) / 50f
        val roll = Math.atan2((rightEye.y - leftEye.y).toDouble(), (rightEye.x - leftEye.x).toDouble()).toFloat()

        return FacePose(yaw, pitch, roll)
    }
}