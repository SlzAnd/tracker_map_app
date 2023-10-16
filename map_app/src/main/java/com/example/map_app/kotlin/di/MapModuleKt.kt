package com.example.map_app.kotlin.di

import com.example.map_app.kotlin.data.PathRepositoryImplKt
import com.example.map_app.kotlin.domain.GetPath
import com.example.map_app.kotlin.domain.PathRepositoryKt
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModuleKt {

    @Provides
    @Singleton
    fun providePathRepository(): PathRepositoryKt {
        return PathRepositoryImplKt()
    }

    @Provides
    @Singleton
    fun provideGetPathUseCase(repository: PathRepositoryKt) : GetPath {
        return GetPath(repository)
    }
}