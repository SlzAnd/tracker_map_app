package com.example.map_app.kotlin.data

import com.example.authentication.kotlin.getUserUID
import com.example.map_app.kotlin.domain.PathRepositoryKt
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class PathRepositoryImplKt : PathRepositoryKt {

    private val LAT = "latitude"
    private val LNG = "longitude"
    private val TIME_STAMP = "time"

    var firestore = FirebaseFirestore.getInstance()

    override fun getPath(date: LocalDate): Flow<List<LatLng>> {
        val collectionName = getUserUID()
        return callbackFlow {

            firestore.collection(collectionName)
                .get()
                .addOnCompleteListener { task: Task<QuerySnapshot> ->
                    if (task.isSuccessful) {

                        val tasks = task.result.getDocuments().filter { documentSnapshot ->
                            convertToLocalDate(
                                documentSnapshot.getTimestamp(TIME_STAMP)!!
                            ).isEqual(date)
                        }

                        if (tasks.isNotEmpty()) {
                            trySend(tasks.map { documentSnapshot ->
                                LatLng(
                                    documentSnapshot.getDouble(LAT)!!,
                                    documentSnapshot.getDouble(LNG)!!
                                )
                            })
                            close()
                        } else {
                            trySend(emptyList())
                            close()
                        }
                    } else if (task.isComplete) {
                        close()
                    } else {
                        error("Firebase task was cancelled")
                    }
                }
            awaitClose { close() }
        }
    }

    private fun convertToLocalDate(timestamp: Timestamp): LocalDate {
        val zoneId = ZoneId.systemDefault()
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000),
            zoneId
        ).toLocalDate()
    }
}