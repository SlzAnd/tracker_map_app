package com.example.tracker_task.data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.tracker_task.BuildConfig;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.functions.Cancellable;

public class DefaultLocationClient implements LocationClient {
    private final Context context;
    private final FusedLocationProviderClient client;

    public DefaultLocationClient(Context context, FusedLocationProviderClient client) {
        this.context = context;
        this.client = client;
    }

    @SuppressLint("MissingPermission")
    @Override
    public Observable<Location> getLocationUpdates() {
        int interval = BuildConfig.TRACKING_PERIOD; // seconds
        int sensitivity = BuildConfig.TRACKING_SENSITIVITY; // meters

        return Observable.create(emitter -> {
            if (!hasLocationPermission()) {
                emitter.tryOnError(new LocationException("Missing location permission"));
            }
            LocationCallback listener = getLocationListener(emitter);
            OnCompleteListener onCompleteListener = getOnCompleteListener(emitter);

            LocationRequest request = new LocationRequest.Builder(interval * 1000)
                    .setIntervalMillis(interval * 1000)
                    .setMinUpdateIntervalMillis(interval * 1000)
                    .setMinUpdateDistanceMeters(sensitivity)
                    .build();

            try {
                client.getLastLocation().addOnSuccessListener(location -> {
                    if (!emitter.isDisposed() && location != null) {
                        emitter.onNext(location);
                    }
                });
                Task task = client.requestLocationUpdates(request, listener, null);
                task.addOnCompleteListener(onCompleteListener);
            } catch (Exception e) {
                emitter.tryOnError(e);
            }
            emitter.setCancellable(getCancellable(listener));
        });
    }

    private Cancellable getCancellable(LocationCallback listener) {
        return () -> {
            client.removeLocationUpdates(listener);
        };
    }

    private OnCompleteListener getOnCompleteListener(ObservableEmitter<Location> emitter) {
        return task -> {
            if (!task.isSuccessful()) {
                if (task.getException() != null) {
                    emitter.tryOnError(task.getException());
                } else {
                    emitter.tryOnError(
                            new IllegalStateException(
                                    "Can't get location from FusedLocationProviderClient"
                            ));
                }
            }
        };
    }

    private LocationCallback getLocationListener(ObservableEmitter<Location> emitter) {
        return new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (!emitter.isDisposed() && locationResult.getLastLocation() != null) {
                    emitter.onNext(locationResult.getLastLocation());
                } else {
                    Log.d("LocClient", "Got location but emitter already disposed");
                }
            }
        };
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;
    }
}
