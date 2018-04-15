package com.example.joao.yamda.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.joao.yamda.domain.entities.Movie

object HttpRequests {
    private val TAG: String = "HttpRequests"

    private var queue: RequestQueue? = null

    fun init(context: Context) {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(context)
    }

    fun get(url: String, responseCb: (String) -> Unit) {
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response: String -> processResponse(response, responseCb) },
                Response.ErrorListener { requestError(it) })
        // Add the request to the RequestQueue.
        queue?.add(stringRequest)
    }

    fun getImage(movie : Movie, url: String, responseCb: (Bitmap) -> Unit) {
        // Request an image response from the provided URL.
        //Log.i(TAG, "Http Request to image Url $url")
        val imageRequest = ImageRequest(
                url,
                Response.Listener { response : Bitmap -> imageResponse(movie, response, responseCb ) },
                0,
                0,
                ImageView.ScaleType.CENTER_INSIDE,
                Bitmap.Config.ALPHA_8,
                Response.ErrorListener { requestError(it) })
        queue?.add(imageRequest)
    }

    private fun imageResponse(movie : Movie, response: Bitmap, responseCb: (Bitmap) -> Unit) {
        Log.i(TAG, "ImageResponse is: $response")
        movie.image = response
        responseCb(response)
    }

    private fun processResponse(response: String, responseCb: (String) -> Unit) {
        Log.i(TAG, "Response is: $response")
        val res = response.split("\"results")[1].substring(2)
        responseCb(res)
    }

    private fun requestError(error: VolleyError) {
        Log.e(TAG, "The Http response could not be obtained because of the following error: $error")
    }
}