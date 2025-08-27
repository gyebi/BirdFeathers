package com.example.birdfeathers

import com.example.birdfeathers.entity.FeedRequirementEntity

fun getDefaultFeedRequirements(): List<FeedRequirementEntity> = listOf(
        // Broiler
    FeedRequirementEntity(
        nameStage = "Starter",
        birdType = "Broiler",
        growthPhase = "0-2",
        kilosPerPhase = 1.0
    ),
    FeedRequirementEntity(
        nameStage = "Grower",
        birdType = "Broiler",
        growthPhase = "2-4",
        kilosPerPhase = 1.5
    ),
    FeedRequirementEntity(
        nameStage = "Finisher",
        birdType = "Broiler",
        growthPhase = "4-6/8",
        kilosPerPhase = 2.5
    ),

        // Guinea Fowl
    FeedRequirementEntity(
        nameStage = "Starter",
        birdType = "Guinea",
        growthPhase = "0-4",
        kilosPerPhase = 0.8
    ),
    FeedRequirementEntity(
        nameStage = "Grower",
        birdType = "Guinea",
        growthPhase = "4-12",
        kilosPerPhase = 1.5
    ),
    FeedRequirementEntity(
        nameStage = "Finisher",
        birdType = "Guinea",
        growthPhase = "12-20",
        kilosPerPhase = 2.2
    ),

        // Layer (to point of lay, 24 weeks)
    FeedRequirementEntity(
        nameStage = "Starter",
        birdType = "Layer",
        growthPhase = "0-6",
        kilosPerPhase = 2.0
    ),
    FeedRequirementEntity(
        nameStage = "Grower",
        birdType = "Layer",
        growthPhase = "6-18",
        kilosPerPhase = 6.5
    ),
    FeedRequirementEntity(
        nameStage = "Pre-Layer",
        birdType = "Layer",
        growthPhase = "18-24",
        kilosPerPhase = 4.0
    )
    )

