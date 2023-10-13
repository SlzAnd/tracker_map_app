//package com.example.tracker_task.java.data.datastore;
//
//import android.content.Context;
//
//import androidx.datastore.preferences.core.MutablePreferences;
//import androidx.datastore.preferences.core.Preferences;
//import androidx.datastore.preferences.core.PreferencesKeys;
//import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
//import androidx.datastore.rxjava3.RxDataStore;
//
//import io.reactivex.rxjava3.core.Flowable;
//import io.reactivex.rxjava3.core.Single;
//import io.reactivex.rxjava3.schedulers.Schedulers;
//
//public class StoreSettings {
//
//    private final Context context;
//    RxDataStore<Preferences> dataStore;
//
//    private final Preferences.Key<Boolean> IS_TRACKING = PreferencesKeys.booleanKey("is_tracking");
//
//    public StoreSettings(Context context) {
//        this.context = context;
//        dataStore = new RxPreferenceDataStoreBuilder(context, "tracking").build();
//    }
//
//    public Flowable<Boolean> getIsTracking() {
//        return dataStore.data().map(preferences -> preferences.get(IS_TRACKING))
//                .subscribeOn(Schedulers.newThread());
//    }
//
//    public void setIsTracking(Boolean isTracking) {
//        dataStore.updateDataAsync(prefsIn -> {
//            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
//            mutablePreferences.set(IS_TRACKING, isTracking != null ? isTracking : false);
//            return Single.just(mutablePreferences);
//        }).subscribe();
//    }
//}
