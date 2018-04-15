package com.example.joao.yamda.domain.services

import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader

class LruImageCache(cacheSize : Int) : ImageLoader.ImageCache {

    private val memCache = LruCache<String, Bitmap>(cacheSize)

    override fun getBitmap(url: String?): Bitmap? = memCache.get(url)

    override fun putBitmap(url: String?, bitmap: Bitmap?) {
        memCache.put(url, bitmap)
    }
}