package com.example.tracker_task.kotlin.domain.use_case

import javax.inject.Inject

data class TrackerUseCasesKt @Inject constructor(
    val gpsStatus: GpsStatus,
    val startTracking: StartTracking,
    val stopTracking: StopTracking
)
