package com.example.map_app.java.presentation;


import androidx.lifecycle.ViewModel;

import com.example.map_app.java.domain.GetPathUseCase;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.time.LocalDate;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class MapViewModel extends ViewModel {

    private GetPathUseCase getPathUseCase;
    private final MapState _state = new MapState();
    public MapState state = _state;

    @Inject
    public MapViewModel(GetPathUseCase getPathUseCase) {
        this.getPathUseCase = getPathUseCase;
    }

    public void getPath(LocalDate date) {
        PolylineOptions polylineOptions = new PolylineOptions();
        getPathUseCase.getPath(date).subscribe(new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull LatLng latLng) {
                polylineOptions.add(latLng);
                _state.setPolyline(polylineOptions);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
                _state.setPolyline(null);
            }
        });
    }
}
