package com.example.joao.yamda.handler

import android.content.ContentResolver
import android.content.AsyncQueryHandler
import android.database.Cursor
import android.net.Uri
import android.util.Log

/**
 * Asynchronous Query Handler to do all the operations over the DB asynchonously
 */
class MyQueryHandler(
        contentResolver: ContentResolver,
        private val asyncQueryListener : ((Cursor?) -> Unit)? = null,
        private val asyncInsertListener: ((Uri?) -> Unit)? = null,
        private val asyncUpdateListener: ((Int) -> Unit)? = null,
        private val asyncDeleteListener: ((Int) -> Unit)? = null) : AsyncQueryHandler(contentResolver) {

    private val TAG = MyQueryHandler::class.java.simpleName


    override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor) {
        Log.i(TAG, "select completed")

        if(asyncQueryListener === null ) return
        asyncQueryListener.invoke(cursor)
    }

    override fun onInsertComplete(token: Int, cookie: Any?, uri: Uri) {
         Log.i(TAG, "insert completed")

        if(asyncInsertListener === null ) return
        asyncInsertListener.invoke(uri)
    }

    override fun onUpdateComplete(token: Int, cookie: Any?, result: Int) {
        Log.i(TAG, "update completed")

        if(result > 0){
            if(asyncUpdateListener === null) return
            asyncUpdateListener.invoke(result)
        }
    }

    override fun onDeleteComplete(token: Int, cookie: Any?, result: Int) {
        Log.i(TAG, "delete completed")
        if(result > 0) {
            if (asyncDeleteListener === null) return
            asyncDeleteListener.invoke(result)
        }
    }
}
