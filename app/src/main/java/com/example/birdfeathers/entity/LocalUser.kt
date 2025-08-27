package com.example.birdfeathers.entity

// com.example.birdfeathers.entity.LocalUser
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class LocalUser(
    @PrimaryKey val uid: String,
    val email: String? = null,
    val displayName: String? = null,
    val phone: String? = null,
    val photoUrl: String? = null
)