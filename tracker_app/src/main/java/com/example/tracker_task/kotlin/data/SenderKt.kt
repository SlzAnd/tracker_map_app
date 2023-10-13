package com.example.tracker_task.kotlin.data

import android.location.Location

interface SenderKt {

    var isFirstPostponed: Boolean
    fun sendToFirebaseImmediately(location: Location)

    fun sendToFirebasePostponed(location: Location)
    fun sendToFirebase(data: HashMap<String, Comparable<*>>)
}