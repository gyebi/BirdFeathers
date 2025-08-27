package com.example.birdfeathers.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.birdfeathers.entity.LocalUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: LocalUser)

    @Update
    suspend fun update (user: LocalUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: LocalUser)

    @Delete
    suspend fun delete(user: LocalUser)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): LocalUser?


    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<LocalUser>

    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    fun observeUser(uid: String): Flow<LocalUser?>

    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    suspend fun getByUid(uid: String): LocalUser?


}