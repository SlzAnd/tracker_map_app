package com.example.map_app.kotlin.domain

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetPath @Inject constructor(
    private val repository: PathRepositoryKt
) {
    fun getPath(date: LocalDate): Flow<List<LatLng>> {
        return repository.getPath(date)
    }
}