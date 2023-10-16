package com.example.map_app.kotlin.domain

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PathRepositoryKt {

    fun getPath(date: LocalDate): Flow<List<LatLng>>
}