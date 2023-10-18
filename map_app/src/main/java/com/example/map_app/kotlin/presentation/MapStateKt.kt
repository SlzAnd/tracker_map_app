package com.example.map_app.kotlin.presentation

import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

sealed class MapStateKt {

    data object ShowDialog : MapStateKt()
    data object HideDialog : MapStateKt()
    data class ShowPath(val flow: Flow<PolylineOptions>, val date: LocalDate) : MapStateKt()
}