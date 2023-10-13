package com.example.tracker_task.kotlin.domain.repository

import androidx.lifecycle.LiveData

interface TrackerRepositoryKt {

    fun startTrackingLocation()

    fun stopTrackingLocation()

    fun getGpsStatusListener(): LiveData<Boolean>
}