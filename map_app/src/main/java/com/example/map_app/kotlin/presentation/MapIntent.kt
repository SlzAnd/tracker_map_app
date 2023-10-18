package com.example.map_app.kotlin.presentation

import java.time.LocalDate

sealed class MapIntent {
    data class GetPath(val date: LocalDate) : MapIntent()
    data object ShowDialog : MapIntent()
    data object HideDialog : MapIntent()
}