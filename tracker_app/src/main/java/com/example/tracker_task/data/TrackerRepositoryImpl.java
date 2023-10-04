package com.example.tracker_task.data;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.tracker_task.domain.repository.TrackerRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TrackerRepositoryImpl implements TrackerRepository {

    private final String TAG = "TRACKER_REPO";
    LocationClient locationClient;
    StatusListeners statusListeners;
    Sender sender;
    private final CompositeDisposable disposables = new CompositeDisposable();
    Context context;


    public TrackerRepositoryImpl(
            LocationClient locationClient,
            StatusListeners statusListeners,
            Sender sender,
            Context context
    ) {
        this.locationClient = locationClient;
        this.statusListeners = statusListeners;
        this.sender = sender;
        this.context = context;
    }

    @Override
    public void startTrackingLocation() {

        disposables.clear();
        locationClient.getLocationUpdates()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Location location) {
                        try {
                            if (statusListeners.isNetworkEnabled()) {
                                sender.sendToFirebaseImmediately(location);
                            } else {
                                sender.sendToFirebasePostponed(location);
                            }
                        } catch (Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.w(TAG, "" + e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void stopTrackingLocation() {
        disposables.clear();
    }

    @Override
    public LiveData<Boolean> getGpsStatusListener() {
        return statusListeners.getGpsStatusListener();
    }
}
