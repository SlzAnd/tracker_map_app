package com.example.tracker_task.kotlin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracker_task.kotlin.data.datastore.StoreSettingsKt
import com.example.tracker_task.kotlin.domain.use_case.TrackerUseCasesKt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerViewModelKt @Inject constructor(
    private val useCases: TrackerUseCasesKt,
    private val dataStore: StoreSettingsKt,
): ViewModel() {

    private val _state: TrackerStateKt = TrackerStateKt()
    var state: TrackerStateKt = _state

    init {
        viewModelScope.launch {
            dataStore.getIsTracking().collect{savedIsTracking ->
                _state.isTracking = savedIsTracking
            }
        }
        _state.gpsStatusListener = useCases.gpsStatus()
    }

    fun onEvent(event: TrackerEventKt) {
        when (event) {
            TrackerEventKt.StartTrackingKt -> {
                useCases.startTracking()
                _state.isTracking = true
                viewModelScope.launch {
                    dataStore.setIsTracking(true)
                }
            }
            TrackerEventKt.StopTrackingKt -> {
                useCases.stopTracking()
                _state.isTracking = false
                viewModelScope.launch {
                    dataStore.setIsTracking(false)
                }
            }
            is TrackerEventKt.SetPermissionState -> {
                _state.setIsPermissionGranted(event.isGranted)
            }
        }
    }
}