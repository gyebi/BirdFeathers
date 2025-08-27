package com.example.birdfeathers

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.birdfeathers.domain.EggCollectionDao
import com.example.birdfeathers.domain.FeedRequirementDao
import com.example.birdfeathers.domain.FlockArrivalDao
import com.example.birdfeathers.domain.UserDao
import com.example.birdfeathers.entity.Chicken
import com.example.birdfeathers.entity.EggCollectionEntity
import com.example.birdfeathers.entity.FeedRequirementEntity
import com.example.birdfeathers.entity.FlockArrivalEntity
import com.example.birdfeathers.entity.LocalUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [FlockArrivalEntity::class,
    EggCollectionEntity::class,
    LocalUser::class,
    Chicken::class,
    FeedRequirementEntity::class]
    , version = 2,
    exportSchema = false)

abstract class BirdFeatherDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
        //abstract fun chickenDao(): ChickenDao
        abstract fun eggCollectionDao(): EggCollectionDao
        abstract fun flockArrivalDao(): FlockArrivalDao
        abstract fun feedRequirementDao(): FeedRequirementDao




companion object {
    @Volatile
    private var INSTANCE: BirdFeatherDatabase? = null

        fun getDatabase(context: Context): BirdFeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BirdFeatherDatabase::class.java,
                    "birdfeather_db"
                )
                    .fallbackToDestructiveMigration()
                            // ADD THIS CALLBACK:
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    CoroutineScope(Dispatchers.IO).launch {
                                        Log.d("BirdFeatherDB", "Prepopulating feed_requirements!")

                                        getDatabase(context).feedRequirementDao().insertAll(
                                            getDefaultFeedRequirements()
                                        )
                                    }
                                }
                            })
                            // END CALLBACK
                            .build()
                        INSTANCE = instance
                        instance
                    }
                }

            }
        }