package com.example.birdfeathers.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.birdfeathers.entity.EggCollection

@Entity(tableName = "egg_collection")
data class EggCollectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val timeOfDay: String,
    val eggCount: Int,
    val flockCount: Int,
    val notes: String
)

fun EggCollection.toEntity(): EggCollectionEntity = EggCollectionEntity(
    date = this.date,
    timeOfDay = this.timeOfDay,
    eggCount = this.eggCount,
    flockCount = this.flockCount,
    notes = this.notes
)

fun EggCollectionEntity.toDomain(): EggCollection = EggCollection(
    date = this.date,
    timeOfDay = this.timeOfDay,
    eggCount = this.eggCount,
    flockCount = this.flockCount,
    notes = this.notes
)


