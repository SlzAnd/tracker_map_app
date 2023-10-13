package com.example.tracker_task.kotlin.presentation

sealed class TrackerEventKt{
    data object StartTrackingKt: TrackerEventKt()
    data object StopTrackingKt: TrackerEventKt()
    data class SetPermissionState(val isGranted: Boolean): TrackerEventKt()
}
