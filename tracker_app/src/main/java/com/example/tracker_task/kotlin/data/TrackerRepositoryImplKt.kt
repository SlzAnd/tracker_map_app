package com.example.tracker_task.kotlin.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.tracker_task.kotlin.domain.repository.TrackerRepositoryKt
import com.example.tracker_task.kotlin.isNetworkEnabled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackerRepositoryImplKt @Inject constructor(
    private val context: Context,
    private val locationClient: LocationClientKt,
    private val gpsStatusListener: GpsStatusListenerKt,
    private val sender: SenderKt
) : TrackerRepositoryKt {
    private var trackLocation = false
    override fun startTrackingLocation() {
        trackLocation = true
        val locationsFlow = locationClient.getLocationUpdates()

        GlobalScope.launch(Dispatchers.IO) {
            locationsFlow
                .takeWhile { trackLocation }
                .collect { location ->
                    if (context.isNetworkEnabled()) {
                        sender.sendToFirebaseImmediately(location)
                    } else {
                        sender.sendToFirebasePostponed(location)
                    }
                }
        }
    }

    override fun stopTrackingLocation() {
        trackLocation = false
    }

    override fun getGpsStatusListener(): LiveData<Boolean> {
        return gpsStatusListener
    }
}