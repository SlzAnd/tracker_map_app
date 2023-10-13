package com.example.map_app.java.domain;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDate;

import io.reactivex.rxjava3.core.Observable;

public interface PathRepository {

    Observable<LatLng> getPath(LocalDate date);
}
