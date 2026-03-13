package com.arifilham.liveness_detection.challenges

class LivenessChallengeEngine {
    private val challenges = ChallengeType.entries.shuffled()
    private var index = 0

    fun next(): ChallengeType? {
        if(index >= challenges.size)
            return null

        return challenges[index++]
    }
}