package com.example.tracker_task.kotlin.domain.use_case

import androidx.lifecycle.LiveData
import com.example.tracker_task.kotlin.domain.repository.TrackerRepositoryKt

class GpsStatus(
    private val repository: TrackerRepositoryKt
) {
    operator fun invoke(): LiveData<Boolean> {
        return repository.getGpsStatusListener()
    }
}