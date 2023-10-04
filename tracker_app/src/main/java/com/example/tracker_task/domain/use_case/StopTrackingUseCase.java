package com.example.tracker_task.domain.use_case;

import com.example.tracker_task.domain.repository.TrackerRepository;

public class StopTrackingUseCase implements TrackerUseCase {

    TrackerRepository repository;

    public StopTrackingUseCase(TrackerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        repository.stopTrackingLocation();
    }
}
