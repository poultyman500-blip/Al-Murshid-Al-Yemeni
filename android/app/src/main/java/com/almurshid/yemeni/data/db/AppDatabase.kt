package com.almurshid.yemeni.data.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val distanceMeters: Double = 0.0,
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val routeJson: String = "[]"
)

@Entity(tableName = "waypoints")
data class WaypointEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tripId: Long = 0L,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val capturedAt: Long = 0L
)

@Entity(tableName = "shared_trips")
data class SharedTripEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tripId: Long = 0,
    val sharedAt: Long = 0L,
    val shareId: String = "",
    val likes: Int = 0,
    val views: Int = 0,
    val isPublic: Boolean = false
)

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val type: ChallengeType,
    val targetValue: Double,
    val startDate: Long,
    val endDate: Long,
    val iconEmoji: String
)

enum class ChallengeType {
    DISTANCE_KM,
    TRIPS_COUNT,
    STREAK_DAYS,
    SINGLE_TRIP_KM
}

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val challengeId: String,
    val currentValue: Double,
    val completed: Boolean = false,
    val completedAt: Long? = null
)

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip: TripEntity): Long

    @Query("SELECT * FROM trips ORDER BY startTime DESC")
    fun observeAll(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :tripId LIMIT 1")
    suspend fun getById(tripId: Long): TripEntity?
}

@Dao
interface WaypointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(waypoint: WaypointEntity): Long

    @Query("SELECT * FROM waypoints WHERE tripId = :tripId ORDER BY capturedAt ASC")
    fun observeForTrip(tripId: Long): Flow<List<WaypointEntity>>
}

@Dao
interface SharedTripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SharedTripEntity): Long

    @Query("SELECT * FROM shared_trips WHERE shareId = :shareId LIMIT 1")
    suspend fun getByShareId(shareId: String): SharedTripEntity?

    @Query("UPDATE shared_trips SET views = views + 1 WHERE shareId = :shareId")
    suspend fun incrementViews(shareId: String)
}

@Dao
interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(challenge: ChallengeEntity)

    @Query("SELECT * FROM challenges WHERE endDate > :now ORDER BY startDate DESC")
    fun getActiveAt(now: Long): Flow<List<ChallengeEntity>>

    @Query("SELECT * FROM challenges WHERE endDate > :now")
    suspend fun getAllOnceAt(now: Long): List<ChallengeEntity>

    fun getActive(): Flow<List<ChallengeEntity>> = getActiveAt(System.currentTimeMillis())
    suspend fun getAllOnce(): List<ChallengeEntity> = getAllOnceAt(System.currentTimeMillis())
}

@Dao
interface UserProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: UserProgressEntity)

    @Query("SELECT * FROM user_progress WHERE challengeId = :challengeId LIMIT 1")
    fun get(challengeId: String): Flow<UserProgressEntity?>
}
