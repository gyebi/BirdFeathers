package com.example.birdfeathers.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.birdfeathers.data.EggStatsEntry
import com.example.birdfeathers.entity.EggCollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EggCollectionDao {

    @Query("SELECT * FROM egg_collection WHERE date = :date")
    suspend fun getEggCollectionsByDate(date: String): List<EggCollectionEntity>

    // query data used to populate chart table
    @Query("SELECT * FROM egg_collection ORDER by date Asc")
    fun getAllEggCollections(): Flow<List<EggCollectionEntity>>

    @Insert
    suspend fun insertEggCollection(entity: EggCollectionEntity)

    @Query("SELECT * FROM egg_collection ORDER BY date DESC, timeOfDay ASC")
    fun getAllEggs(): Flow<List<EggCollectionEntity>>

    // ✅ Total eggs for a specific date (morning + evening combined)
    @Query("SELECT SUM(eggCount) FROM egg_collection WHERE date = :selectedDate")
    fun getTotalEggsForDate(selectedDate: String): Flow<Int?>


    // Total number of Laying Hens in the coop for a specific date
    @Query("SELECT SUM(flockCount) FROM egg_collection WHERE date = :selectedDate")
    fun getTotalLayingHensForDate(selectedDate: String): Flow<Int?>

    // (Optional) Live version if you want auto-updates without manual refresh for egg count

    @Query("""
        SELECT COALESCE(SUM(eggCount), 0)
        FROM egg_collection
        WHERE date = :date
    """)

    fun observeTotalEggsForDate(date: String): Flow<Int>



    // (Optional) Live version if you want auto-updates without manual refresh for flock count
    @Query("""
        SELECT COALESCE(SUM(flockCount), 0)
        FROM egg_collection
        WHERE date = :date
    """)

    fun observeTotalFlockForDate(date: String): Flow<Int>

    // (Optional) Breakdown morning
    @Query("""
        SELECT COALESCE(SUM(eggCount), 0)
        FROM egg_collection
        WHERE date = :date AND timeOfDay = 'Morning'
    """)

    suspend fun getMorningTotal(date: String): Int

    //evening
    @Query("""
        SELECT COALESCE(SUM(eggCount), 0)
        FROM egg_collection
        WHERE date = :date AND timeOfDay = 'Evening'
    """)
    suspend fun getEveningTotal(date: String): Int

    //eggs collected between a period of time
    @Query("""
    SELECT date AS date, SUM(eggCount) AS eggCount, 
    90.0 as targetLay
    FROM egg_collection
    WHERE date BETWEEN :start AND :end
    GROUP BY date
    ORDER BY date
""")
    suspend fun getEggStatsBetweenDates(start: String, end: String): List<EggStatsEntry>
}
