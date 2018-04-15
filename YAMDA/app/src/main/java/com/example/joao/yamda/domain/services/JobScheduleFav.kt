package com.example.joao.yamda.domain.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.example.joao.yamda.R
import com.example.joao.yamda.domain.entities.Movie
import com.example.joao.yamda.utils.MovieApp
import com.example.joao.yamda.view.SearchActivity
import java.util.*

/**
 * JobService to create notifications in the release day of a movie
 */
@RequiresApi(Build.VERSION_CODES.O)
class JobScheduleFav : JobService() {

    private val TAG = JobScheduleFav::class.java.simpleName

    companion object {
        val JOB_ID = 123450
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        if(MovieApp.sp.getBoolean(MovieApp.NOTIFICATION_KEY, true)){
            Log.i(TAG, "create notification !!")
            Async(this, MovieApp.Notifications.CHANNEL_ID1).execute()
            return true // it's doing asynchronous work
        }
        return false //doesn't have anything to do
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i(TAG, "Favourite Job stooped!!")
        return true //need reschedule
    }

    /**
     * Asynchronous task to search in DB for movies that have been released or if it's the release day
     * Create many notifications as the number of movies found
     */
    private class Async(ctx: JobScheduleFav, chanelId : String): AsyncTask<Void, Void, Void>() {
        private val job = ctx
        private val chanelId = chanelId


        override fun doInBackground(vararg params: Void?): Void? {
            MovieAppService.searchFav{
                it.forEach {
                    job.showNotification(chanelId, it)
                    MovieAppService.deleteFav(it)
                }
            }
            return null
        }
    }

    fun showNotification(chId: String, movie : Movie) {

        val notification = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val operation  = PendingIntent.getActivity(this, 0, Intent(this, SearchActivity::class.java), 0)

        val builder = Notification.Builder(this, chId)
                .setContentTitle("Release day of ")
                .setContentText("${movie.title}     ${movie.release_date}")
                .setSmallIcon(R.drawable.image)
                .setAutoCancel(true)
                .setContentIntent(operation)

        val randomId = Random().nextInt(9999 - 1000) + 1000

        notification.notify(randomId, builder.build())
    }
}

