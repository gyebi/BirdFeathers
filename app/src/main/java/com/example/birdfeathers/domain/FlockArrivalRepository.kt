package com.example.birdfeathers.domain

import com.example.birdfeathers.data.FlockArrival
import com.example.birdfeathers.entity.toEntity
import com.google.firebase.firestore.FirebaseFirestore

class FlockArrivalRepository(
    private val dao: FlockArrivalDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

) {
    suspend fun saveFlockArrival(arrival: FlockArrival) {
        // Save to Room
        dao.insertFlockArrival(arrival.toEntity())
        // Save to Firebase
        firestore.collection("flock_arrivals")
            .add(arrival)

    }



    val allFlockArrivals = dao.getAllFlockArrivals()

}
