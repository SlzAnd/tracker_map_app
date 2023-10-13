package com.example.tracker_task.kotlin.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tracker_task.kotlin.domain.model.MyLocationKt

@Dao
interface LocationDaoKt {

    @Insert
    fun insert(location: MyLocationKt)

    @Query("DELETE FROM MyLocationKt")
    fun deleteAll()

    @Query("SELECT * FROM MyLocationKt")
    fun getAllLocations(): List<MyLocationKt>
}