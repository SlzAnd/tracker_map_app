package com.example.tracker_task.data.data_source;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tracker_task.domain.model.MyLocation;

@Database(entities = {MyLocation.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "locations.db";

    public abstract LocationDAO locationDAO();
}
