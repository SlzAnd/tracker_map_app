//package com.example.tracker_task.java.data;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.location.LocationManager;
//import android.net.ConnectivityManager;
//
//import androidx.lifecycle.LiveData;
//
//public class StatusListeners {
//
//    private Context context;
//
//    public StatusListeners(Context context) {
//        this.context = context;
//    }
//
//    public LiveData<Boolean> getGpsStatusListener() {
//        return new GpsStatusListener();
//    }
//
//    public boolean isNetworkEnabled() {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        return cm.getActiveNetwork() != null;
//    }
//
//    private class GpsStatusListener extends LiveData<Boolean> {
//
//        @Override
//        protected void onActive() {
//            registerReceiver();
//            checkGpsStatus();
//        }
//
//        @Override
//        protected void onInactive() {
//            unregisterReceiver();
//        }
//
//        private BroadcastReceiver gpsStatusReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                checkGpsStatus();
//            }
//        };
//
//        private void checkGpsStatus() {
//            if (isLocationEnabled()) {
//                postValue(true);
//            } else {
//                postValue(false);
//            }
//        }
//
//        private boolean isLocationEnabled() {
//            return context.getSystemService(LocationManager.class).isProviderEnabled(LocationManager.GPS_PROVIDER);
//        }
//
//        private void registerReceiver() {
//            context.registerReceiver(gpsStatusReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
//        }
//
//        private void unregisterReceiver() {
//            context.unregisterReceiver(gpsStatusReceiver);
//        }
//    }
//}
