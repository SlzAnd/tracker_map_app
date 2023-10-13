//package com.example.tracker_task.java.presentation;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import java.util.Objects;
//
//public class TrackerState {
//    private boolean isTracking = false;
//
//    private LiveData<Boolean> gpsStatusListener = null;
//    private MutableLiveData<Boolean> _isPermissionGranted = new MutableLiveData<>(true);
//    private LiveData<Boolean> isPermissionGranted = _isPermissionGranted;
//
//    public TrackerState() {
//    }
//
//    public boolean isTracking() {
//        return isTracking;
//    }
//
//    public void setTracking(boolean tracking) {
//        isTracking = tracking;
//    }
//
//    public LiveData<Boolean> getGpsStatusListener() {
//        return gpsStatusListener;
//    }
//
//    public void setGpsStatusListener(LiveData<Boolean> gpsStatusListener) {
//        this.gpsStatusListener = gpsStatusListener;
//    }
//
//    public LiveData<Boolean> isPermissionGranted() {
//        return isPermissionGranted;
//    }
//
//    public void set_isPermissionGranted(boolean isPermissionGranted) {
//        this._isPermissionGranted.postValue(isPermissionGranted);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        TrackerState that = (TrackerState) o;
//        return isTracking == that.isTracking && Objects.equals(gpsStatusListener, that.gpsStatusListener);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(isTracking, gpsStatusListener);
//    }
//}
