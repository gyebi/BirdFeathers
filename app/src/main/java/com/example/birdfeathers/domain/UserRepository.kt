
package com.example.birdfeathers.domain

import com.example.birdfeathers.entity.LocalUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao
) {
    fun currentUid(): String? = auth.currentUser?.uid

    /** UI should read from this (fast/offline). */
    fun observeLocalUser(): Flow<LocalUser?> {
        val uid = currentUid() ?: return flowOf(null)
        return userDao.observeUser(uid)
    }

    /** Write to Room. */
    suspend fun upsertLocalUser(user: LocalUser) {
        userDao.upsert(user)
    }

    /** Write/merge to Firestore: users/{uid}. */
    suspend fun upsertRemoteUser(
        uid: String,
        email: String?,
        displayName: String?
    ) {
        val data = hashMapOf(
            "uid" to uid,
            "email" to email,
            "displayName" to displayName
        ).filterValues { it != null }

        firestore.collection("users")
            .document(uid)
            .set(data, SetOptions.merge())
            .await()
    }

    suspend fun getUserByEmailFromRoom(email: String): LocalUser? =
        userDao.getUserByEmail(email)

    suspend fun saveUserToRoom(user: LocalUser) =
        userDao.upsert(user)

    suspend fun getUserByUidFromRoom(uid: String): LocalUser? = userDao.getByUid(uid)
        //suspend fun upsertLocalUser(user: LocalUser) = userDao.upsert(user)


    /** Pull from Firestore → cache to Room. */
    suspend fun refreshUserFromRemote() {
        val uid = currentUid() ?: return
        val doc = firestore.collection("users").document(uid).get().await()
        if (doc.exists()) {
            upsertLocalUser(
                LocalUser(
                    uid = uid,
                    email = doc.getString("email"),
                    displayName = doc.getString("displayName"),
                    phone = doc.getString("phone"),
                    photoUrl = doc.getString("photoUrl")
                )
            )
        }
    }
}
