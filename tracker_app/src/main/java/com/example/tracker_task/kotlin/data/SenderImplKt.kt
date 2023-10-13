package com.example.tracker_task.kotlin.data

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.authentication.kotlin.getUserUID
import com.example.tracker_task.kotlin.data.data_source.LocationDaoKt
import com.example.tracker_task.kotlin.domain.model.MyLocationKt
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SenderImplKt(
    private val dao: LocationDaoKt,
    private val context: Context
): SenderKt {

    private val firestore = FirebaseFirestore.getInstance()
    override var isFirstPostponed = true
    private val TAG = "SenderImplKt"

    override fun sendToFirebaseImmediately(location: Location) {
        val data = hashMapOf(
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "time" to Timestamp.now()
        )
        sendToFirebase(data)
    }

    override fun sendToFirebasePostponed(location: Location) {
        val loc = MyLocationKt(latitude = location.latitude, longitude = location.longitude)
        GlobalScope.launch {
            dao.insert(loc)
        }

        if (isFirstPostponed) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val sendRequest = OneTimeWorkRequest.Builder(SendWorkerKt::class.java)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .enqueue(sendRequest)

            isFirstPostponed = false
        }
    }

    override fun sendToFirebase(data: HashMap<String, Comparable<*>>) {
        val userUID = getUserUID()
        if (userUID != null) {
            firestore.collection(userUID)
                .add(data)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}") }
                .addOnFailureListener { Log.w(TAG, "Error adding document", it) }
        }
    }
}