package com.example.tracker_task.kotlin.di

import android.app.Application
import android.content.Context
import androidx.room.Room.databaseBuilder
import com.example.tracker_task.kotlin.data.DefaultLocationClientKt
import com.example.tracker_task.kotlin.data.GpsStatusListenerKt
import com.example.tracker_task.kotlin.data.LocationClientKt
import com.example.tracker_task.kotlin.data.SenderImplKt
import com.example.tracker_task.kotlin.data.SenderKt
import com.example.tracker_task.kotlin.data.TrackerRepositoryImplKt
import com.example.tracker_task.kotlin.data.data_source.LocationDaoKt
import com.example.tracker_task.kotlin.data.data_source.LocationDatabaseKt
import com.example.tracker_task.kotlin.data.datastore.StoreSettingsKt
import com.example.tracker_task.kotlin.domain.repository.TrackerRepositoryKt
import com.example.tracker_task.kotlin.domain.use_case.GpsStatus
import com.example.tracker_task.kotlin.domain.use_case.StartTracking
import com.example.tracker_task.kotlin.domain.use_case.StopTracking
import com.example.tracker_task.kotlin.domain.use_case.TrackerUseCasesKt
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerModuleKt {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext appContext: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(appContext)
    }

    @Provides
    @Singleton
    fun provideLocationClient(
        @ApplicationContext appContext: Context,
        client: FusedLocationProviderClient
    ): LocationClientKt {
        return DefaultLocationClientKt(appContext, client)
    }

    @Provides
    @Singleton
    fun provideGpsStatusListener(@ApplicationContext appContext: Context): GpsStatusListenerKt {
        return GpsStatusListenerKt(appContext)
    }

    @Provides
    @Singleton
    fun provideTrackerRepository(
        client: LocationClientKt,
        gpsStatusListener: GpsStatusListenerKt,
        sender: SenderKt,
        @ApplicationContext context: Context
    ): TrackerRepositoryKt {
        return TrackerRepositoryImplKt(
            locationClient = client,
            gpsStatusListener = gpsStatusListener,
            sender = sender,
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideTrackerUseCases(repository: TrackerRepositoryKt): TrackerUseCasesKt {
        return TrackerUseCasesKt(
            GpsStatus(repository),
            StartTracking(repository),
            StopTracking(repository)
        )
    }

    @Provides
    @Singleton
    fun provideLocationDatabase(application: Application): LocationDatabaseKt {
        return databaseBuilder(
            application,
            LocationDatabaseKt::class.java,
            LocationDatabaseKt.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSender(
        @ApplicationContext context: Context,
        database: LocationDatabaseKt
    ): SenderKt {
        return SenderImplKt(database.dao(), context)
    }

    @Provides
    @Singleton
    fun provideDao(database: LocationDatabaseKt): LocationDaoKt {
        return database.dao()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): StoreSettingsKt {
        return StoreSettingsKt(context)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}