package com.example.tracker_task.domain.use_case;

import com.example.tracker_task.domain.repository.TrackerRepository;

public class StartTrackingUseCase implements TrackerUseCase {

    TrackerRepository repository;

    public StartTrackingUseCase(TrackerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        repository.startTrackingLocation();
    }
}
