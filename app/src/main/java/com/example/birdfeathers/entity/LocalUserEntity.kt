package com.example.birdfeathers.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
/*
@Entity(tableName = "users")
data class LocalUser(
    @PrimaryKey val email: String,
    val hashedPassword: String,
    val name: String
)

 */

@Entity(tableName = "chickens")
data class Chicken(
    @PrimaryKey (autoGenerate = true) val id: Int,
    val name: String,
    val isActive: String,
    val lastSeen: Long
)



