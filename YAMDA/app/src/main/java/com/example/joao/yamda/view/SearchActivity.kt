package com.example.joao.yamda.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import com.example.joao.yamda.R
import com.example.joao.yamda.domain.entities.Operation
import com.example.joao.yamda.domain.services.RefreshService
import kotlinx.android.synthetic.main.home.*

class SearchActivity : Activity() {
    private val TAG : String = SearchActivity::class.simpleName!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home)

        val language = resources.getString(R.string.language)
        searchText.text.clear()

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

        posterButton.setOnClickListener{
            Log.i(TAG,"Click on Now_playing Button" )
            startOtherActivity("now_playing", null, language)
        }

        premiereButton.setOnClickListener{
            Log.i(TAG,"Click on Premiere Button" )
            startOtherActivity("upcoming", null, language)
        }

        popularButton.setOnClickListener{
            Log.i(TAG,"Click on Popular Button" )
            startOtherActivity("popular", null, language)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater

        inflater.inflate(R.menu.main_menu, menu )

        return super .onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.i(TAG, "onOptionsItemSelected")
        when(item?.itemId) {
            R.id.sharedPreferences  -> this.startActivity(Intent(this, SharedPreferencesActivity::class.java))
            R.id.aboutUs -> this.startActivity(Intent(this, ProfileActivity::class.java))
            R.id.refresh -> this.startService(Intent(this, RefreshService::class.java))
        }

        return super.onOptionsItemSelected(item);
    }

    private fun startOtherActivity(name: String, search: String?, language: String) {
        val intent = Intent(this, ListMovieActivity::class.java)
        intent.putExtra("input",  Operation(name, search, language))
        startActivity(intent)
    }

    private fun startNoConnectivityActivity(){
        val intent = Intent(this, NoConnectivityActivity::class.java)
        startActivity(intent)
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo == null
    }

    private fun search(language : String) {
        if(isNetworkAvailable())
            startNoConnectivityActivity()
        else{
            val generalText = resources.getString(R.string.searchText)
            val text = searchText.text.toString()
            if (text != "" && text != generalText) {
                Log.i(TAG, "Click on Search Button and you Search: $text")
                startOtherActivity("search", text, language)
            }
        }
    }
}