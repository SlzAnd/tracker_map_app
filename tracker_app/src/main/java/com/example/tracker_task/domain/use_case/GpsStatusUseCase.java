package com.example.tracker_task.domain.use_case;

import androidx.lifecycle.LiveData;

import com.example.tracker_task.domain.repository.TrackerRepository;

public class GpsStatusUseCase {
    TrackerRepository repository;

    public GpsStatusUseCase(TrackerRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> execute() {
        return repository.getGpsStatusListener();
    }

    ;
}
