package com.example.map_app.java.di;

import com.example.map_app.java.data.PathRepositoryImpl;
import com.example.map_app.java.domain.GetPathUseCase;
import com.example.map_app.java.domain.PathRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MapModule {

    @Provides
    @Singleton
    public static PathRepository providePathRepository() {
        return new PathRepositoryImpl();
    }

    @Provides
    @Singleton
    public static GetPathUseCase provideGetPathUseCase(PathRepository repository) {
        return new GetPathUseCase(repository);
    }
}
