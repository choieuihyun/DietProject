package com.myproject.dietproject

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DietProjectApplication : Application() {

    private lateinit var mReceiver: BatteryReceiver

    override fun onCreate() {
        super.onCreate()

        mReceiver = BatteryReceiver()

        val intentFilter = IntentFilter()

        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)

        intentFilter.addAction(mReceiver.batteryAction)
        registerReceiver(mReceiver, intentFilter)

        val intent = Intent(mReceiver.batteryAction)
        sendBroadcast(intent)
    }

    override fun onTerminate() {
        super.onTerminate()

        unregisterReceiver(mReceiver)
    }


}