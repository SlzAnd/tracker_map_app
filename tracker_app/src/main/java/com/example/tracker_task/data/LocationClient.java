package com.example.tracker_task.data;


import android.location.Location;

import io.reactivex.rxjava3.core.Observable;

public interface LocationClient {

    Observable<Location> getLocationUpdates();

    class LocationException extends Exception {
        public LocationException(String message) {
            super(message);
        }
    }
}
