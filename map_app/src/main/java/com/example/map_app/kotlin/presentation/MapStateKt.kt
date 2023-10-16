package com.example.map_app.kotlin.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate

data class MapStateKt(
    var isShownDialog: Boolean = false,
    val _selectedDate: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now()),
    val selectedDate: LiveData<LocalDate> = _selectedDate,
) {
    fun setDate(date: LocalDate) {
        _selectedDate.postValue(date)
    }
}