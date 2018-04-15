package com.example.joao.yamda.view

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.joao.yamda.R
import com.example.joao.yamda.domain.adapters.MovieListAdapter
import com.example.joao.yamda.domain.entities.Operation
import com.example.joao.yamda.domain.services.MovieAppService
import com.example.joao.yamda.utils.MovieApp
import kotlinx.android.synthetic.main.movies_presentation.*

class ListMovieActivity : ListActivity() {
    private val TAG : String = ListMovieActivity::class.simpleName!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language = MovieApp.language
        setContentView(R.layout.movies_presentation)
        init()

        searchText.setOnKeyListener(View.OnKeyListener {_, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                search(language)
                searchText.text.clear()
                return@OnKeyListener true
            }
            false
        })

        searchButton.setOnClickListener{
            search(language)
        }

        homeButton.setOnClickListener{
            Log.i(TAG, "click on back button")
            super.onBackPressed()
        }
    }

    private fun init() {
        val searchObj = intent.getParcelableExtra<Operation>("input")

        when(searchObj.action) {
            "search"-> {
                Log.i(TAG, "obteve $searchObj")

                setMyAction(searchObj, true)
            }
            "now_playing"-> {
                Log.i(TAG, "obteve $searchObj")

                setMyAction(searchObj, false)
            }
            "upcoming"-> {
                Log.i(TAG, "obteve $searchObj")

                setMyAction(searchObj, false)
            }
            "popular"->{
                Log.i(TAG, "obteve $searchObj")

                setMyAction(searchObj, false)
            }
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        Log.i(TAG, "Clicked on view to the object: ${v.tag}")

        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("detail", v.tag as Parcelable )

        startActivity(intent)
    }

    private fun setMyAction(name: Operation, searchOrNot: Boolean) {

        showProgressBar()

        MovieAppService.searchMovies(name, searchOrNot) {
            listAdapter = MovieListAdapter(it,
                    { it.title },
                    { it.poster_path },
                    layoutInflater)
        }
    }

    private fun showProgressBar() {
        val root = findViewById<View>(android.R.id.content) as ViewGroup

        listView.emptyView = layoutInflater.inflate(R.layout.progress_bar, null, false)
        root.addView(listView.emptyView)
    }

    private fun search(language : String){

        val generalText = resources.getString(R.string.searchText)
        val text = searchText.text.toString()

        if (text != "" && text != generalText) {
            Log.i(TAG, "Click on Search Button and you Search: $text")
            setMyAction(Operation("search", text, language), true)
        }
    }
}