package com.example.tracker_task.data.data_source;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tracker_task.domain.model.MyLocation;

import java.util.List;

@Dao
public interface LocationDAO {

    @Insert
    void insert(MyLocation myLocation);

    @Query("DELETE FROM mylocation")
    void deleteAll();

    @Query("SELECT * FROM mylocation")
    List<MyLocation> getAllLocations();
}
