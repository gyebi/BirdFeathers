package com.example.birdfeathers.entity

data class EggCollection(    val date: String,      // e.g., "2024-07-09"
                             val timeOfDay: String, // "Morning" or "Evening"
                             val eggCount: Int,
                             val flockCount: Int,
                             val notes: String
)