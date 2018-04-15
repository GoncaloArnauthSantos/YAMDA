package com.example.joao.yamda.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.example.joao.yamda.R
import com.example.joao.yamda.domain.services.FavouriteService
import kotlinx.android.synthetic.main.shared_preferences.*
import com.example.joao.yamda.domain.services.MovieAppService
import com.example.joao.yamda.receiver.BootReceiver
import com.example.joao.yamda.utils.MovieApp

class SharedPreferencesActivity : Activity(){

    private val TAG : String = SharedPreferencesActivity::class.simpleName!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.shared_preferences)
        init()
        showPreferences()
    }

    fun init(){
        val spEdit = MovieApp.sp.edit()
        homeButton.setOnClickListener{
            Log.i(TAG, "click on back button")
            super.onBackPressed()

        }

        notificationButton.setOnClickListener{
            if(notificationButton.isChecked){
                notificationButton.isChecked = true
                spEdit.putBoolean(MovieApp.NOTIFICATION_KEY, true)
                spEdit.apply()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    MovieAppService.scheduleJobsFav(this, 0)
                else
                    startService(Intent(this, FavouriteService::class.java))

            } else {
                notificationButton.isChecked = false
                spEdit.putBoolean(MovieApp.NOTIFICATION_KEY, false)
                spEdit.commit()
            }
            showPreferences()
        }

        wifiButton.setOnClickListener {
            if(wifiButton.isChecked){
                wifiButton.isChecked = true
                anyButton.isChecked = false
                spEdit.putBoolean(MovieApp.WIFI_KEY, true)
                spEdit.putBoolean(MovieApp.ANY_KEY, false)
                spEdit.commit()
            }
            showPreferences()
        }

        anyButton.setOnClickListener {
            if(anyButton.isChecked){
                wifiButton.isChecked = false
                anyButton.isChecked = true
                spEdit.putBoolean(MovieApp.WIFI_KEY, false)
                spEdit.putBoolean(MovieApp.ANY_KEY, true)
                spEdit.commit()
            }
            showPreferences()
        }

        updateButton.setOnClickListener{
            val current = updateTime.text.toString()
            if( current != ""){
                spEdit.putString(MovieApp.UPDATE_KEY, current)
                spEdit.commit()
            }
            showPreferences()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                MovieApp.scheduleJobsApi()

            else
                sendBroadcast(Intent(this, BootReceiver::class.java))
        }
    }

    private fun showPreferences(){
        notificationButton.isChecked = MovieApp.sp.getBoolean(MovieApp.NOTIFICATION_KEY, true)

        wifiButton.isChecked = MovieApp.sp.getBoolean(MovieApp.WIFI_KEY, false)
        anyButton.isChecked = MovieApp.sp.getBoolean(MovieApp.ANY_KEY,true)
        updateCurrent.text = MovieApp.sp.getString(MovieApp.UPDATE_KEY, "15")
    }
}