package com.example.joao.yamda.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class Notifications(context: Context) {
    val CHANNEL_ID1 = "MovieAppChannel1"
    val MOVIE_APP_CHANNEL_NAME = "MovieApp"

    init {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(nm.getNotificationChannel(CHANNEL_ID1) == null) {
            // Create Channel 1
            val notificationChannel1 = NotificationChannel(CHANNEL_ID1, MOVIE_APP_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel1.enableLights(true)
            nm.createNotificationChannel(notificationChannel1)
        }
    }

}