package com.example.joao.yamda.domain.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import com.example.joao.yamda.data.CoimaRepository
import com.example.joao.yamda.domain.entities.Movie
import com.example.joao.yamda.domain.entities.Operation
import com.example.joao.yamda.domain.converters.toDomain

object MovieAppService {

    var Repository: CoimaRepository? = null

    fun searchMovies(search: Operation, searchAction: Boolean, movies: (List<Movie>) -> Unit) {
        if (searchAction)
            Repository?.searchMovies(search) { movies(it.map { it.toDomain() }) }
        else
            Repository?.getMovies(search) { movies(it.map { it.toDomain() }) }
    }

    fun updateMovies(language : String){

        val nowPlaying = Operation("now_playing",null, language)
        val upcoming = Operation("upcoming",null, language)
        val popular = Operation("popular",null, language)
        val list : List<Operation> = listOf(upcoming, popular, nowPlaying)

        Repository?.updateTables(list)
    }

    fun createFav(movie : Movie){
        Repository?.addFavourite(movie)
    }

    fun deleteFav(movie: Movie) {
        Repository?.removeFavourite(movie)
    }

    fun searchFav(movies: (List<Movie>) -> Unit){
        Repository?.searchFavourite { movies(it.map {it.toDomain()}) }
    }

    /**
     * create a schedule to wake up the JobService after "schedule" milliseconds
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleJobsFav(ctx : Context, schedule : Long) {
        val builder = JobInfo.Builder(JobScheduleFav.JOB_ID,
                ComponentName(ctx, JobScheduleFav::class.java))

        val jobScheduler = ctx.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.setPersisted(true).setMinimumLatency(schedule).setRequiresBatteryNotLow(true).build())
    }

    /**
     * create a alarm to wake up the Service after "schedule" milliseconds
     */
    fun alarmFav(ctx : Context, schedule : Long){

        val operation = PendingIntent.getService(ctx, 0, Intent(ctx, FavouriteService::class.java), 0)

        val alarm = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarm.set(AlarmManager.RTC, schedule, operation)
    }
}