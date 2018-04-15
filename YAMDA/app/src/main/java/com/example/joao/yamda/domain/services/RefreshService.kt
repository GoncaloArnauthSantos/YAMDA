package com.example.joao.yamda.domain.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.example.joao.yamda.utils.MovieApp

/**
 * Service in a separated worker thread to update data from API
 */
class RefreshService : IntentService("RefreshService") {

    private val TAG : String = RefreshService::class.simpleName!!

    override fun onHandleIntent(intent: Intent?) {

        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        if( isConected(activeNetwork) ){
            //verify if the current connectivity is compatible with the user preferences
            val myWifi = MovieApp.sp.getBoolean(MovieApp.WIFI_KEY, false)
            if(myWifi && !isWifiOn(activeNetwork)){}
            else{
                Log.i(TAG, "updating from API")
                MovieAppService.updateMovies(MovieApp.language)
            }
        }

    }
    private fun isWifiOn(net : NetworkInfo) : Boolean{
        return net.type ==ConnectivityManager.TYPE_WIFI
    }
    private fun isConected(net : NetworkInfo?) : Boolean{
        return net != null && net.isConnectedOrConnecting
    }
}
