package com.example.tracker_task.domain.repository;

import androidx.lifecycle.LiveData;

public interface TrackerRepository {

    void startTrackingLocation();

    void stopTrackingLocation();

    LiveData<Boolean> getGpsStatusListener();
}
