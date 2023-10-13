package com.example.tracker_task.kotlin.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tracker_task.kotlin.domain.model.MyLocationKt

@Database(entities = [MyLocationKt::class], version = 1)
abstract class LocationDatabaseKt : RoomDatabase() {
    abstract fun dao(): LocationDaoKt

    companion object {
        const val DATABASE_NAME = "locations.db"
    }
}