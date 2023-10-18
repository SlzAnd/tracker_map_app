package com.example.tracker_task.kotlin.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.example.tracker_task.BuildConfig
import com.example.tracker_task.kotlin.hasLocationPermission

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultLocationClientKt @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClientKt {
    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<Location> {
        val interval = BuildConfig.TRACKING_PERIOD // seconds
        val sensitivity = BuildConfig.TRACKING_SENSITIVITY // meters

        return callbackFlow {
            if (!context.hasLocationPermission()) {
                throw LocationClientKt.LocationException("Missing location permission")
            }

            val request = LocationRequest.Builder(interval * 1000L)
                .setIntervalMillis(interval * 1000L)
                .setMinUpdateIntervalMillis(interval * 1000L)
                .setMinUpdateDistanceMeters(sensitivity.toFloat())
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}