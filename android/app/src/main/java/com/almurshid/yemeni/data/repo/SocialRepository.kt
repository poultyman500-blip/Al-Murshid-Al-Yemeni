package com.almurshid.yemeni.data.repo

import com.almurshid.yemeni.data.db.TripDao
import com.almurshid.yemeni.data.db.TripEntity
import kotlinx.coroutines.flow.Flow

class TripRepository(
    private val tripDao: TripDao
) {
    val allTrips: Flow<List<TripEntity>> = tripDao.observeAll()

    suspend fun addTrip(trip: TripEntity): Long = tripDao.insert(trip)

    suspend fun getTrip(tripId: Long): TripEntity? = tripDao.getById(tripId)
}
