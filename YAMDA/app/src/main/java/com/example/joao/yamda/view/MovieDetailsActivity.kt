package com.example.joao.yamda.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.example.joao.yamda.R
import com.example.joao.yamda.domain.entities.Movie
import com.example.joao.yamda.domain.services.MovieAppService
import kotlinx.android.synthetic.main.movie_details.*
import com.example.joao.yamda.utils.MovieApp
import java.util.*

class MovieDetailsActivity : Activity() {

    private val TAG : String = MovieDetailsActivity::class.simpleName!!
    private val DELAY : Long = 0
    private lateinit var currentD : String
    private lateinit var releaseD : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = findViewById<View>(android.R.id.content) as ViewGroup
        layoutInflater.inflate(R.layout.progress_bar, root, true)

        Handler().postDelayed({
            init()
        }, DELAY)
    }


    private fun init() {
        setContentView(R.layout.movie_details)

        val spEdit = MovieApp.sp.edit()

        val detail  = intent.getParcelableExtra<Movie>("detail")
        Log.i(TAG, "have : $detail")

        showDetails(detail)

        homeButton.setOnClickListener{
            Log.i(TAG, "click on back button")
            super.onBackPressed()
        }

        favButton.setOnClickListener {
            if( favButton.isChecked ) {
                Log.i(TAG, "check Fav button")

                if( wilBeReleased(detail) ){
                    favButton.isChecked = true
                    spEdit.putBoolean(detail.id.toString(), true)
                    spEdit.commit()

                    MovieAppService.createFav(detail)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        MovieAppService.scheduleJobsFav(this, scheduleDate())
                    else
                        MovieAppService.alarmFav(this, scheduleDate())
                }
                else{
                    favButton.isChecked = false
                    spEdit.putBoolean(detail.id.toString(), false)
                    spEdit.commit()
                }
            } else {
                Log.i(TAG, "uncheck Fav button")
                MovieAppService.deleteFav(detail)
                favButton.isChecked = false
                spEdit.putBoolean(detail.id.toString(), false)
                spEdit.commit()
            }
            showDetails(detail)
        }
    }

    private fun scheduleDate() : Long{
        val curr = stringToMilli(currentD)
        val release = stringToMilli(releaseD)

        return release - curr
    }

    private fun showDetails(detail: Movie){
        vote.text = detail.vote_average.toString()
        titleDetail.text = detail.title
        releaseDate.text = detail.release_date!!

        if(detail.image != null)
            image.setImageBitmap(detail.image)
        else
            image.setImageResource(R.drawable.no_image)

        movieTxt.text = detail.overview
        favButton.isChecked = MovieApp.sp.getBoolean(detail.id.toString(), false)
    }

    private fun wilBeReleased(detail : Movie): Boolean{
        releaseD = detail.release_date!!
        currentD = MovieApp.DATEFORMAT.format(Date())

        return currentD < releaseD
    }

    private fun stringToMilli( value : String): Long {
        return MovieApp.DATEFORMAT.parse(value).time
    }
}