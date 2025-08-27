package com.example.birdfeathers

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreUserManager(private val context: Context) {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuthManager(context)

    fun getUserData(uid: String) = callbackFlow {
        try {
            val userRef = db.collection("users").document(uid)
            val snapshot = userRef.get().await()
            trySend(snapshot.data)
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching user: ${e.localizedMessage}")
            trySend(null)
        }
        awaitClose { /* no-op */ }
    }
}
