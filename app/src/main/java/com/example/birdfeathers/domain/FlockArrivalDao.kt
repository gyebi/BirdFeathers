package com.example.birdfeathers.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.birdfeathers.entity.FlockArrivalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlockArrivalDao {
    @Insert
    suspend fun insertFlockArrival(entity: FlockArrivalEntity)

    @Query("SELECT * FROM flock_arrival ORDER BY arrivalDate DESC")
    fun getAllFlockArrivals(): Flow<List<FlockArrivalEntity>>


}