package com.example.joao.yamda.domain.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log

/**
 * JobService to do updates from the API
 */
@RequiresApi(Build.VERSION_CODES.O)
class JobScheduleApi : JobService() {
    private val TAG = JobScheduleApi::class.java.simpleName

    companion object {
        val JOB_ID = 123452
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(TAG, "update Api !!!!")
        startService(Intent(this, RefreshService::class.java))
        return true // the refreshService is doing on background
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i(TAG, "Api Job stooped !!")
        return true // if stooped need reschedule
    }
}