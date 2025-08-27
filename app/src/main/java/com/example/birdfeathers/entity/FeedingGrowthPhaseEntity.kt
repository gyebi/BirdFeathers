package com.example.birdfeathers.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_requirements")
data class FeedRequirementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nameStage: String,     // "Starter", "Grower", "Finisher"
    val birdType: String,      // "Broiler", "Layer", "Guinea"
    val growthPhase: String,   // "Phase 1", "Phase 2", etc.
    val kilosPerPhase: Double  // Feed per bird per phase
)