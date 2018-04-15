package com.example.joao.yamda.domain.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.joao.yamda.R
import com.example.joao.yamda.domain.entities.Movie
import com.example.joao.yamda.domain.services.LruImageCache
import com.example.joao.yamda.utils.HttpRequests

class MovieListAdapter<T>(private val movies: List<T>, private val textExtractor: (T) -> String, private val urlExtractor: (T)-> String?, private val layoutInflater: LayoutInflater) : BaseAdapter() {

    private val TAG : String = MovieListAdapter::class.simpleName!!

    private val imageCache = LruImageCache(movies.size)

    override fun getItem(idx: Int): Any = movies[idx] as Any

    override fun getItemId(idx: Int): Long = idx.toLong()

    override fun getCount(): Int = movies.size

    override fun getView(position: Int, convertView: View?, container: ViewGroup): View {

        Log.i(TAG, "getView called with - position: $position")

        var convertView = convertView
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.movie_list, container, false)

        val retView : View = convertView!!

        val item: T = movies[position]
        retView.tag = item

        val image = convertView.findViewById<ImageView>(R.id.image)

        val tag = retView.tag

        val imageUrl = "http://image.tmdb.org/t/p/w185/" + urlExtractor(item)

        val bitmapFromCache = imageCache.getBitmap(imageUrl)

        if(bitmapFromCache != null) image.setImageBitmap(bitmapFromCache)

        else
            HttpRequests.getImage(item as Movie, imageUrl) {
                if (tag == retView.tag) {
                    imageCache.putBitmap(imageUrl, it)
                    image.setImageBitmap(it)
                }
            }

        if(bitmapFromCache == null) image.setImageResource(R.drawable.no_image)

        val title = convertView.findViewById<TextView>(R.id.title)
        title?.text = textExtractor(item)

        return retView
    }
}