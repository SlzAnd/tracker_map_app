package com.example.map_app.kotlin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.map_app.kotlin.domain.GetPath
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MapViewModelKt @Inject constructor(
    private val getPathUseCase: GetPath,
) : ViewModel() {

    val mapIntent = Channel<MapIntent>(Channel.UNLIMITED)
    private val _state =
        MutableStateFlow<MapStateKt>(MapStateKt.ShowPath(getPath(LocalDate.now()), LocalDate.now()))
    val state: StateFlow<MapStateKt>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            mapIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is MapIntent.GetPath -> {
                        _state.value = MapStateKt.ShowPath(getPath(intent.date), intent.date)
                    }

                    MapIntent.ShowDialog -> {
                        _state.value = MapStateKt.ShowDialog
                    }

                    MapIntent.HideDialog -> {
                        _state.value = MapStateKt.HideDialog
                    }
                }
            }
        }
    }

    private fun getPath(date: LocalDate): Flow<PolylineOptions> {
        return getPathUseCase.getPath(date).map { PolylineOptions().addAll(it) }
    }
}