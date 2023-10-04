package com.example.tracker_task.presentation;

import androidx.lifecycle.LiveData;

import java.util.Objects;

public class TrackerState {
    private boolean isTracking = false;

    private LiveData<Boolean> gpsStatusListener = null;


    public TrackerState() {
    }


    public boolean isTracking() {
        return isTracking;
    }

    public void setTracking(boolean tracking) {
        isTracking = tracking;
    }

    public LiveData<Boolean> getGpsStatusListener() {
        return gpsStatusListener;
    }

    public void setGpsStatusListener(LiveData<Boolean> gpsStatusListener) {
        this.gpsStatusListener = gpsStatusListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackerState that = (TrackerState) o;
        return isTracking == that.isTracking && Objects.equals(gpsStatusListener, that.gpsStatusListener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isTracking, gpsStatusListener);
    }
}
