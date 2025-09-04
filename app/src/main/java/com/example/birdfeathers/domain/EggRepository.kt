package com.example.birdfeathers.domain

import com.example.birdfeathers.data.EggStatsEntry
import com.example.birdfeathers.entity.EggCollection
import com.example.birdfeathers.entity.EggCollectionEntity
import com.example.birdfeathers.entity.toEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class EggRepository(
    private val eggDao: EggCollectionDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {
    val allEggCollections:
            Flow<List<EggCollectionEntity>> = eggDao.getAllEggCollections()

    suspend fun saveEggCollection(entry: EggCollection) {
        eggDao.insertEggCollection(entry.toEntity())
        firestore.collection("egg_collections").add(entry).await()
    }

    suspend fun getTotalEggsForDate(date: String): Flow<Int?> =
        eggDao.getTotalEggsForDate(date)

    suspend fun getTotalLayingHensForDate(date: String): Flow<Int?> =
        eggDao.getTotalLayingHensForDate(date)

    fun observeTotalEggsForDate(date: String): Flow<Int> =
        eggDao.observeTotalEggsForDate(date)

    fun observeTotalHensForDate(date: String): Flow<Int> =
        eggDao.observeTotalFlockForDate(date)

    suspend fun getMorningTotal(date: String) = eggDao.getMorningTotal(date)
    suspend fun getEveningTotal(date: String) = eggDao.getEveningTotal(date)

    suspend fun getEggStatsBetweenDates(start: String, end: String): List<EggStatsEntry> =
        eggDao.getEggStatsBetweenDates(start, end)
}
