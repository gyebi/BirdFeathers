package com.example.birdfeathers.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.birdfeathers.entity.FeedRequirementEntity

@Dao
interface FeedRequirementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(feedList: List<FeedRequirementEntity>)

    @Query("SELECT * FROM feed_requirements WHERE birdType = :birdType")
    suspend fun getFeedByBirdType(birdType: String): List<FeedRequirementEntity>

    @Query("SELECT * FROM feed_requirements")
    suspend fun getAllFeedRequirements(): List<FeedRequirementEntity>

}