package com.example.joao.yamda.provider

import android.provider.BaseColumns
import pt.isel.pdm.profslist.provider.MoviesContract

object DbSchema {

    val DB_NAME = "movies.db"
    val DB_VERSION = 1

    val COL_ID = BaseColumns._ID

    object Movies {
        val TBL_NAME = "movies"

        val COL_FAVOURITES = MoviesContract.Movies.FAVOURITES
        val COL_UPCOMING = MoviesContract.Movies.UPCOMING
        val COL_NOW_PLAYING = MoviesContract.Movies.NOW_PLAYING
        val COL_POPULAR = MoviesContract.Movies.POPULAR
        val COL_MOVIE_ID = MoviesContract.Movies.MOVIE_ID
        val COL_TITLE = MoviesContract.Movies.TITLE
        val COL_POSTER_PATH = MoviesContract.Movies.POSTER_PATH
        val COL_VOTE_AVERAGE = MoviesContract.Movies.VOTE_AVERAGE
        val COL_OVERVIEW = MoviesContract.Movies.OVERVIEW
        val COL_RELEASE_DATE = MoviesContract.Movies.RELEASE_DATE

        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + "(" +
                        COL_ID + " INTEGER PRIMARY KEY, " +
                        COL_MOVIE_ID + " INTEGER UNIQUE, " +
                        COL_TITLE + " TEXT, " +
                        COL_POSTER_PATH + " TEXT UNIQUE, " +
                        COL_VOTE_AVERAGE + " INTEGER, " +
                        COL_OVERVIEW + " TEXT, " +
                        COL_RELEASE_DATE + " TEXT, " +
                        COL_FAVOURITES + " INTEGER DEFAULT 0, " +
                        COL_UPCOMING + " INTEGER DEFAULT 0, " +
                        COL_NOW_PLAYING + " INTEGER DEFAULT 0, " +
                        COL_POPULAR + " INTEGER DEFAULT 0) "

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }
}