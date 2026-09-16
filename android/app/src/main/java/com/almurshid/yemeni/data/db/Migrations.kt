package com.almurshid.yemeni.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(
    entities = [
        TripEntity::class,
        WaypointEntity::class,
        SharedTripEntity::class,
        ChallengeEntity::class,
        UserProgressEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(ChallengeTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun waypointDao(): WaypointDao
    abstract fun sharedTripDao(): SharedTripDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun userProgressDao(): UserProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "almurshid.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}

class ChallengeTypeConverter {
    @TypeConverter
    fun fromType(type: ChallengeType): String = type.name

    @TypeConverter
    fun toType(value: String): ChallengeType = ChallengeType.valueOf(value)
}
