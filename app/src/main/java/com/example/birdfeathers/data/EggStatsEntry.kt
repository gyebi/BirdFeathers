package com.example.birdfeathers.data

data class EggStatsEntry(
    val date: String,         // e.g. "2025-08-24"
    val eggCount: Int,        // actual count
    val targetLay: Float      // percentage target, e.g. 80f
)
