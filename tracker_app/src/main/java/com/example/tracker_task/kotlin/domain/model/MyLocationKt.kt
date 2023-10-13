package com.example.tracker_task.kotlin.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyLocationKt(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
)
