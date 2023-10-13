//package com.example.tracker_task.java;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.hilt.work.HiltWorkerFactory;
//import androidx.work.Configuration;
//
//import javax.inject.Inject;
//
//import dagger.hilt.android.HiltAndroidApp;
//
//@HiltAndroidApp
//public class TrackerApplication extends Application implements Configuration.Provider {
//
//    @Inject
//    HiltWorkerFactory workerFactory;
//
//    @NonNull
//    @Override
//    public Configuration getWorkManagerConfiguration() {
//        return new Configuration.Builder().setWorkerFactory(workerFactory).build();
//    }
//}