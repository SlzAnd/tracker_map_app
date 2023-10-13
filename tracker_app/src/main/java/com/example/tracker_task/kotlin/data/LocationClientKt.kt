package com.example.tracker_task.kotlin.data

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClientKt {

    fun getLocationUpdates(): Flow<Location>

    class LocationException(message: String): Exception()
}