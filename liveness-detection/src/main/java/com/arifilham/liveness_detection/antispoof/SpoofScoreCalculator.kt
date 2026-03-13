package com.arifilham.liveness_detection.antispoof

import com.arifilham.liveness_detection.models.LivenessState

class SpoofScoreCalculator {
    fun score(state: LivenessState): Int {
        var score = 0

        if(state.blink) score += 30
        if(state.headMove) score += 30
        if(state.motion) score += 20
        if(state.textureOk) score += 20

        return score
    }
}