package com.example.tracker_task.kotlin.domain.use_case

import com.example.tracker_task.kotlin.domain.repository.TrackerRepositoryKt

class StopTracking(
    private val repository: TrackerRepositoryKt
) {
    operator fun invoke() {
        repository.stopTrackingLocation()
    }
}