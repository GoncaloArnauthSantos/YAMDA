package com.example.joao.yamda

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.example.joao.yamda.data.CoimaApiRepository
import com.example.joao.yamda.domain.services.JobScheduleApi
import com.example.joao.yamda.domain.services.MovieAppService
import com.example.joao.yamda.utils.HttpRequests
import com.example.joao.yamda.utils.Notifications
import java.text.SimpleDateFormat

class MovieApp : Application(){
    private val TAG: String = "MovieApp"

    val PREFS_PRIVATE = "PREFS_PRIVATE"
    val NOTIFICATION_KEY = "NotificationKey"
    val WIFI_KEY = "WifiKey"
    val ANY_KEY = "AnyKey"
    val UPDATE_KEY = "UpdateKey"
    val DATEFORMAT = SimpleDateFormat("yyyy-MM-dd")
    lateinit var language : String

    lateinit var Notifications: Notifications
    lateinit var  sp: SharedPreferences
    private val MINUTE_TO_MILLIS = 60_000
    private var isUpdateEnable : Boolean = false

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG," App Started ----------------------------")

        language = resources.getString(R.string.language)
        sp = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            Notifications = Notifications(this)

        HttpRequests.init(applicationContext)

        CoimaApiRepository.context = contentResolver

        MovieAppService.Repository = CoimaApiRepository

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            scheduleJobsApi()

        isUpdateEnable = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleJobsApi() {
        val builder = JobInfo.Builder(JobScheduleApi.JOB_ID,
                ComponentName(this, JobScheduleApi::class.java))

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val schedule = sp.getString(UPDATE_KEY, "15").toLong() * MINUTE_TO_MILLIS

        //verify which type of network is selected
        val network = if(sp.getBoolean(WIFI_KEY, false)) JobInfo.NETWORK_TYPE_UNMETERED else JobInfo.NETWORK_TYPE_ANY

        //checks if is the first time updating data
        if(isUpdateEnable) jobScheduler.schedule(builder.setPersisted(true).setPeriodic(schedule).setRequiredNetworkType(network).build())
        else jobScheduler.schedule(builder.setPersisted(true).setMinimumLatency(schedule).setRequiredNetworkType(network).setRequiresBatteryNotLow(true).build())
    }
}