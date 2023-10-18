package com.example.tracker_task.kotlin.domain.use_case

import com.example.tracker_task.kotlin.domain.repository.TrackerRepositoryKt
import javax.inject.Inject

class StopTracking @Inject constructor(
    private val repository: TrackerRepositoryKt
) {
    operator fun invoke() {
        repository.stopTrackingLocation()
    }
}