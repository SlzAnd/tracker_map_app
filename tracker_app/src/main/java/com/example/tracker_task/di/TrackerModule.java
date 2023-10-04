package com.example.tracker_task.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.tracker_task.data.DefaultLocationClient;
import com.example.tracker_task.data.LocationClient;
import com.example.tracker_task.data.Sender;
import com.example.tracker_task.data.SenderImpl;
import com.example.tracker_task.data.StatusListeners;
import com.example.tracker_task.data.TrackerRepositoryImpl;
import com.example.tracker_task.data.data_source.LocationDatabase;
import com.example.tracker_task.data.datastore.StoreSettings;
import com.example.tracker_task.domain.use_case.GpsStatusUseCase;
import com.example.tracker_task.domain.use_case.StartTrackingUseCase;
import com.example.tracker_task.domain.use_case.StopTrackingUseCase;
import com.example.tracker_task.domain.repository.TrackerRepository;
import com.example.tracker_task.domain.use_case.TrackerUseCases;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class TrackerModule {

    @Provides
    @Singleton
    public static FusedLocationProviderClient provideFusedLocationProviderClient(@ApplicationContext Context appContext) {
        return LocationServices.getFusedLocationProviderClient(appContext);
    }

    @Provides
    @Singleton
    public static LocationClient provideLocationClient(@ApplicationContext Context appContext, FusedLocationProviderClient client) {
        return new DefaultLocationClient(appContext, client);
    }

    @Provides
    @Singleton
    public static StatusListeners provideStatusListeners(@ApplicationContext Context appContext) {
        return new StatusListeners(appContext);
    }

    @Provides
    @Singleton
    public static TrackerRepository provideTrackerRepository(LocationClient client,
                                                             StatusListeners statusListeners,
                                                             Sender sender,
                                                             @ApplicationContext Context context) {
        return new TrackerRepositoryImpl(client, statusListeners, sender, context);
    }

    @Provides
    @Singleton
    public static TrackerUseCases provideTrackerUseCases(TrackerRepository repository) {
        return new TrackerUseCases(
                new StartTrackingUseCase(repository),
                new StopTrackingUseCase(repository),
                new GpsStatusUseCase(repository)
        );
    }

    @Provides
    @Singleton
    public static LocationDatabase provideLocationDatabase(Application application) {
        return Room.databaseBuilder(
                application,
                LocationDatabase.class,
                LocationDatabase.DATABASE_NAME
        ).build();
    }

    @Provides
    @Singleton
    public static Sender provideSender(@ApplicationContext Context context, LocationDatabase database) {
        return new SenderImpl(database.locationDAO(), context);
    }

    @Provides
    @Singleton
    public static StoreSettings provideDataStore(@ApplicationContext Context context) {
        return new StoreSettings(context);
    }
}
