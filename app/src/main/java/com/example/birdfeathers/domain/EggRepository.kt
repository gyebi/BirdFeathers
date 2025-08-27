package com.example.birdfeathers.domain

import com.example.birdfeathers.entity.EggCollection
import com.example.birdfeathers.entity.toEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class EggRepository(
    private val eggDao: EggCollectionDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),

    ) {
    suspend fun saveEggCollection(entry: EggCollection) {
        // Save to Room
        eggDao.insertEggCollection(entry.toEntity())

        // Save to Firestore
        firestore.collection("egg_collections")
            .add(entry)
            .await()
        //.addOnSuccessListener { Log.d("EggRepo", "Saved to Firestore") }
        //.addOnFailureListener { Log.e("EggRepo", "Save failed: ${it.message}") }
    }

        // stream all entries in Room Dbase
        val allEggCollections = eggDao.getAllEggCollections()
        //suspend fun saveEggCollection(entry: EggCollection) =
        //eggDao.insert(entry.toEntity())

    //eggs collected for a specific date
    suspend fun getTotalEggsForDate(date: String): Flow<Int?> =
        eggDao.getTotalEggsForDate(date)

    // Total number of Laying Hens in the coop for a specific date
    suspend fun getTotalLayingHensForDate(date: String): Flow<Int?> =
        eggDao.getTotalLayingHensForDate(date)


    //this is what i am using
    fun observeTotalEggsForDate(date: String): Flow<Int> {
        return eggDao.observeTotalEggsForDate(date)
    }

    // daily hen count for a specific date
    fun observeTotalHensForDate(date: String): Flow<Int> {
        return eggDao.observeTotalFlockForDate(date)
    }


    // Optional breakdown
    suspend fun getMorningTotal(date: String) = eggDao.getMorningTotal(date)
    suspend fun getEveningTotal(date: String) = eggDao.getEveningTotal(date)
}