package com.example.tracker_task.kotlin.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class TrackerStateKt(
    var isTracking: Boolean = false,
    var gpsStatusListener: LiveData<Boolean>? = null,
    private val _isPermissionGranted: MutableLiveData<Boolean> = MutableLiveData(true),
    val isPermissionGranted: LiveData<Boolean> = _isPermissionGranted
) {
    fun setIsPermissionGranted(isGranted: Boolean) {
        _isPermissionGranted.postValue(isGranted)
    }
}
