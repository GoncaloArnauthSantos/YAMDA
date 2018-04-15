package com.example.joao.yamda.domain.services

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.joao.yamda.R
import com.example.joao.yamda.domain.entities.Movie
import com.example.joao.yamda.utils.MovieApp
import com.example.joao.yamda.view.SearchActivity
import java.util.*

/**
 * Service in a separated worker thread to launch notifications in the release day of the movies
 */

class FavouriteService : IntentService("FavouriteService") {

    private val TAG : String = FavouriteService::class.simpleName!!

    override fun onHandleIntent(intent: Intent?) {

        if(MovieApp.sp.getBoolean(MovieApp.NOTIFICATION_KEY, true)) {
            Log.i(TAG, "Release Day !!")
            MovieAppService.searchFav {
                it.forEach {
                    showNotification(it)
                    MovieAppService.deleteFav(it)
                }
            }
        }
    }

    private fun showNotification(movie: Movie) {

        val notification = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val operation  = PendingIntent.getActivity(this, 0, Intent(this, SearchActivity::class.java), 0)

        val builder = NotificationCompat.Builder(this)
                .setTicker("New message arrived")
                .setContentTitle("Release day of ")
                .setContentText("${movie.title}     ${movie.release_date}")
                .setSmallIcon(R.drawable.image)
                .setAutoCancel(true)
                .setContentIntent(operation)

        val randomId = Random().nextInt(9999 - 1000) + 1000

        notification.notify(randomId, builder.build())
    }

}
