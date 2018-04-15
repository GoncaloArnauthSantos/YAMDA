package com.example.joao.yamda.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.example.joao.yamda.MovieApp
import com.example.joao.yamda.domain.services.RefreshService

/**
 * wake up when the boot of the device is done or update time preferences has chanched
 */

class BootReceiver : BroadcastReceiver() {

    private val UPDATE_KEY = "UpdateKey"
    private val MINUTE_TO_MILLIS = 60_000
    private val PREFS_PRIVATE = "PREFS_PRIVATE"

    private val TAG : String = BootReceiver::class.simpleName!!

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.i(TAG, " onReceived UPDATE")
        val prefs = context!!.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE)
        var updateTime = prefs.getString(UPDATE_KEY, "15").toLong() * MINUTE_TO_MILLIS


        val operation = PendingIntent.getService(context, 0, Intent(context, RefreshService::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(updateTime.toInt() == 0){
            alarm.cancel(operation)
            Log.i(TAG, " cancelling updates from API ")
        }
        else{
            alarm.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), updateTime, operation)
            Log.i(TAG, " update will repeat in :  $updateTime  millis")
        }
    }
}
