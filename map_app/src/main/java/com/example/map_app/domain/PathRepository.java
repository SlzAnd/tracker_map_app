package com.example.map_app.domain;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDate;

import io.reactivex.rxjava3.core.Observable;

public interface PathRepository {

    Observable<LatLng> getPath(LocalDate date);
}
