package pt.isel.pdm.profslist.provider

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

object MoviesContract {

    val AUTHORITY = "com.example.joao.yamda.provider"

    val CONTENT_PROVIDER_URI = Uri.parse("content://" + AUTHORITY)

    val MEDIA_BASE_SUBTYPE = "/vnd.movieslist."

    object Movies : BaseColumns {
        val RESOURCE = "movies"

        val CONTENT_URI = Uri.withAppendedPath(
                MoviesContract.CONTENT_PROVIDER_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val ID = BaseColumns._ID

        val FAVOURITES = "favourites"
        val UPCOMING = "upcoming"
        val NOW_PLAYING = "now_playing"
        val POPULAR = "popular"
        val MOVIE_ID = "movie_id"
        val TITLE = "title"
        val POSTER_PATH = "poster_path"
        val VOTE_AVERAGE = "vote_average"
        val OVERVIEW = "overview"
        val RELEASE_DATE = "release_date"

        val PROJECT_ALL = arrayOf(ID, FAVOURITES, UPCOMING, NOW_PLAYING, POPULAR, MOVIE_ID, TITLE, POSTER_PATH, VOTE_AVERAGE, OVERVIEW, RELEASE_DATE)

        val DEFAULT_SORT_ORDER = RELEASE_DATE + " DESC"

    }

    fun customSortOrder(columnName: Array<String>, ascending: Boolean): String =
        columnName.joinToString { s -> s } + if(ascending) " ASC" else " DESC"


}