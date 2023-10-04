package com.example.tracker_task.presentation;

import androidx.lifecycle.ViewModel;

import com.example.tracker_task.data.datastore.StoreSettings;
import com.example.tracker_task.domain.use_case.TrackerUseCases;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TrackerViewModel extends ViewModel {
    TrackerUseCases useCases;
    StoreSettings dataStore;


    @Inject
    public TrackerViewModel(TrackerUseCases useCases, StoreSettings dataStore) {
        this.useCases = useCases;
        this.dataStore = dataStore;
        _state = new TrackerState();
        boolean isTracking = dataStore.getIsTracking().blockingFirst();
        _state.setTracking(isTracking);
        _state.setGpsStatusListener(useCases.getGpsStatusUseCase().execute());
        state = _state;
    }

    private final TrackerState _state;
    private TrackerState state;

    public TrackerState getState() {
        return state;
    }

    public void onEvent(TrackerEvent event) {
        if (event instanceof StartTracking) {
            useCases.getStartTrackingUseCase().execute();
            _state.setTracking(true);
            dataStore.setIsTracking(true);
        } else if (event instanceof StopTracking) {
            useCases.getStopTrackingUseCase().execute();
            _state.setTracking(false);
            dataStore.setIsTracking(false);
        }
    }
}
