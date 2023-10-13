package com.example.tracker_task.kotlin.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.LiveData
import com.example.tracker_task.kotlin.isLocationEnabled

class GpsStatusListenerKt(
    private val context: Context
) : LiveData<Boolean>() {
    override fun onActive() {
        registerReceiver()
        checkGpsStatus()
    }

    override fun onInactive() {
        unregisterReceiver()
    }

    private val gpsStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkGpsStatus()
        }
    }

    private fun checkGpsStatus() {
        if (context.isLocationEnabled()) {
            postValue(true)
        } else {
            postValue(false)
        }
    }

    private fun registerReceiver() {
        context.registerReceiver(
            gpsStatusReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
    }

    private fun unregisterReceiver() {
        context.unregisterReceiver(gpsStatusReceiver)
    }
}