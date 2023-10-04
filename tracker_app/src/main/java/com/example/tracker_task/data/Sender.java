package com.example.tracker_task.data;

import android.location.Location;


public interface Sender {

    void sendToFirebaseImmediately(Location location);

    void sendToFirebasePostponed(Location location);
}
