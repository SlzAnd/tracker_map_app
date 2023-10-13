package com.example.tracker_task.kotlin.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tracker_task.kotlin.data.data_source.LocationDaoKt
import com.google.firebase.Timestamp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SendWorkerKt @AssistedInject constructor(
    private val dao: LocationDaoKt,
    private val sender: SenderKt,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val myLocations = dao.getAllLocations()
        if (myLocations.isNotEmpty()) {
            myLocations.forEach { myLocation ->
                val location = hashMapOf(
                    "latitude" to myLocation.latitude,
                    "longitude" to myLocation.longitude,
                    "time" to Timestamp.now(),
                )
                sender.sendToFirebase(location)
            }
            dao.deleteAll()
        }
        sender.isFirstPostponed = true
        return Result.success()
    }
}