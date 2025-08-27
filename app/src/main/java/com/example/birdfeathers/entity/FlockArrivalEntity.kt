package com.example.birdfeathers.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.birdfeathers.data.FlockArrival

enum class BirdType (val displayValue: String) {
    BROILER("Broiler"),
    LAYER("Layer"),
    GUINEA("Guinea")
}



@Entity(tableName = "flock_arrival")

data class FlockArrivalEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val arrivalDate: String,
    val farmName: String,
    val coopNumber: String,
    val birdType: String,
    val dayOldChicks: Int
)

// Mapper functions
fun FlockArrival.toEntity(): FlockArrivalEntity {
    return FlockArrivalEntity(
        arrivalDate = arrivalDate,
        farmName = farmName,
        coopNumber = coopNumber,
        birdType = birdType,
        dayOldChicks = dayOldChicks
    )
}

fun FlockArrivalEntity.toDomain(): FlockArrival {
    return FlockArrival(
        arrivalDate = arrivalDate,
        farmName = farmName,
        coopNumber = coopNumber,
        birdType = birdType,
        dayOldChicks = dayOldChicks
    )
}