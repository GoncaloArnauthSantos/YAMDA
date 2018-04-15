package com.example.joao.yamda.data

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.example.joao.yamda.data.dtos.MovieDto
import com.example.joao.yamda.domain.converters.toDomain
import com.example.joao.yamda.domain.entities.Movie
import com.example.joao.yamda.domain.entities.Operation
import com.example.joao.yamda.handler.MyQueryHandler
import com.example.joao.yamda.utils.HttpRequests
import com.example.joao.yamda.utils.JsonConverter
import pt.isel.pdm.profslist.provider.MoviesContract
import java.text.SimpleDateFormat
import java.util.*

object CoimaApiRepository : CoimaRepository {

    private val BASE_URI = "https://api.themoviedb.org/3"

    private val TAG : String = CoimaApiRepository::class.simpleName!!

    var context: ContentResolver? = null

    private val contentUri = MoviesContract.Movies.CONTENT_URI
    private val baseColumnsId = MoviesContract.Movies.ID
    private val listName = MoviesContract.Movies.UPCOMING
    private val movieId = MoviesContract.Movies.MOVIE_ID
    private val title = MoviesContract.Movies.TITLE
    private val posterPath = MoviesContract.Movies.POSTER_PATH
    private val voteAverage = MoviesContract.Movies.VOTE_AVERAGE
    private val overview = MoviesContract.Movies.OVERVIEW
    private val releaseDate = MoviesContract.Movies.RELEASE_DATE
    private var idCounter = 0

    val from = arrayOf(listName, movieId, title, posterPath, voteAverage, overview, releaseDate);

    /**
     * When it's done a normal search
     */
    override fun searchMovies(searchStr: Operation, cb: (List<MovieDto>) -> Unit) {
        HttpRequests.get(
                "$BASE_URI/search/movie?api_key=940b97cffc9b5f91012e29019b60affb&language=${searchStr.language}&query=${searchStr.search!!.replace(" ", "%20", true)}",
                { obj -> cb(JsonConverter.convert(obj) ) }
        )
    }

    /**
     * Check if the DB is empty if it is get the movies from API and save them in DB, If not get the movies from DB
     */
    override fun getMovies(action: Operation, cb: (List<MovieDto>) -> Unit) {
        MyQueryHandler(context!!, {
            if (it!!.count == 0)
                getMoviesFromApi(action) {
                    cb( it.map {
                        addMovieInDB(action, it)
                        it
                    })
                }
            else
                cursorToMovieList(it, cb)
        }).startQuery(1, null, contentUri, from,action.action + " = ?", arrayOf("1"), MoviesContract.customSortOrder(arrayOf(MoviesContract.Movies.RELEASE_DATE), false))
    }

    /**
     * if that movie is in the DB do update, if not do the insert
     */
    private fun addMovieInDB(action: Operation, movie: MovieDto) {
        MyQueryHandler(context!!, {
            if (it!!.count != 0) {
                val contentValues = ContentValues()
                contentValues.put(action.action, 1)
                MyQueryHandler(context!!)
                        .startUpdate(1, null, contentUri, contentValues, "$movieId = ?", arrayOf("${movie.id}"))
            } else
                MyQueryHandler(context!!)
                        .startInsert(1, null, contentUri, createMovieContentValues(movie.toDomain(), action.action))

        }).startQuery(1, null, contentUri, from, "$movieId  = ?", arrayOf("${movie.id}"), MoviesContract.customSortOrder(arrayOf(MoviesContract.Movies.RELEASE_DATE), false))
    }

    override fun updateTables(lst : List<Operation>) {

        context!!.delete(contentUri,null,null)

        lst.map{
            var operation = it
            getMoviesFromApi(it) {
                it.map {

                    val movieCursor = context!!.query(contentUri, from, "$movieId  = ?", arrayOf("${it.id}"), MoviesContract.customSortOrder(arrayOf(MoviesContract.Movies.RELEASE_DATE), false))
                    try {
                        if (movieCursor.count != 0) {
                            val contentValues = ContentValues()
                            contentValues.put(operation.action, 1)
                            context!!.update(contentUri, contentValues, "$movieId = ?", arrayOf("${it.id}"))
                        } else
                            context!!.insert(contentUri, createMovieContentValues(it.toDomain(), operation.action))
                    } finally {
                        movieCursor.close()
                    }
                }
            }
        }
    }

    override fun addFavourite(movie: Movie){
        val contentValues = ContentValues()
        contentValues.put("favourites", 1)
        MyQueryHandler(context!!, { Log.i(TAG, " ${movie.title} added as Favourite") })
                .startUpdate(1, null, contentUri, contentValues, "$movieId = ?", arrayOf("${movie.id}"))
    }

    override fun removeFavourite(movie: Movie) {
        val contentValues = ContentValues()
        contentValues.put("favourites", 0)
        MyQueryHandler(context!!, { Log.i(TAG, " ${movie.title} removed as Favourite") })
                .startUpdate(1, null, contentUri, contentValues, "$movieId = ?", arrayOf("${movie.id}"))
    }

    /**
     * check in DB if any movie it will release today or if some of the released movies did not has been notified
     */
    override fun searchFavourite(cb: (List<MovieDto>) -> Unit) {

        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
//        val date = "2018-02-14"
        MyQueryHandler(context!!, {
            cursorToMovieList(it!!, cb)
            Log.i(TAG, " Searching favourite movies")
        }).startQuery(1,null, contentUri, from, "favourites = ? AND release_date <= ?", arrayOf("1", date), MoviesContract.customSortOrder(arrayOf(MoviesContract.Movies.RELEASE_DATE), false))
    }

    private fun cursorToMovieList(returnCursor: Cursor, cb: (List<MovieDto>) -> Unit){
        try {
            val movieList = ArrayList<MovieDto>()

            while (returnCursor.moveToNext())
                movieList.add(returnCursor.toDomain())

            cb(movieList)
        } finally {
            returnCursor.close()
        }
    }

    private fun createMovieContentValues(movie : Movie, action : String?) : ContentValues {
        val contentValues = ContentValues()
        var upcoming = 0
        var popular = 0
        var now_playing = 0

        contentValues.put(baseColumnsId, ++idCounter)

        when(action){
            "upcoming" -> upcoming = 1
            "popular" -> popular =1
            "now_playing" -> now_playing = 1
        }

        contentValues.put("favourites", 0)
        contentValues.put("upcoming", upcoming)
        contentValues.put("popular", popular)
        contentValues.put("now_playing", now_playing)
        contentValues.put(movieId, movie.id)
        contentValues.put(title, movie.title)
        contentValues.put(posterPath, movie.poster_path)
        contentValues.put(voteAverage, movie.vote_average)
        contentValues.put(overview, movie.overview)
        contentValues.put(releaseDate, movie.release_date)
//        contentValues.put(releaseDate, "2018-02-14")

        return contentValues
    }

    private fun getMoviesFromApi(action: Operation, cb: (List<MovieDto>) -> Unit){
        HttpRequests.get(
                "$BASE_URI/movie/${action.action}?api_key=940b97cffc9b5f91012e29019b60affb&language=${action.language}",
                { obj -> cb(JsonConverter.convert(obj)) }
        )
    }
}