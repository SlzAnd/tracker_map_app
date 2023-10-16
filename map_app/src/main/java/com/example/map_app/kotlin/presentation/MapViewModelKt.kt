package com.example.map_app.kotlin.presentation

import androidx.lifecycle.ViewModel
import com.example.map_app.kotlin.domain.GetPath
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MapViewModelKt @Inject constructor(
    private val getPathUseCase: GetPath,
) : ViewModel() {

    private val _state: MapStateKt = MapStateKt()
    val state = _state

    fun getPath(date: LocalDate): Flow<PolylineOptions> {
        return getPathUseCase.getPath(date).map { PolylineOptions().addAll(it) }
    }
}