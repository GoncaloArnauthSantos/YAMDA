package com.example.joao.yamda.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import pt.isel.pdm.profslist.provider.DbOpenHelper
import pt.isel.pdm.profslist.provider.MoviesContract

class MoviesProvider : ContentProvider() {

    private val MOVIES_LST = 1
    private val MOVIES_OBJ = 2

    private val URI_MATCHER : UriMatcher

    init {
        URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        URI_MATCHER.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.Movies.RESOURCE,
                MOVIES_LST
        )
        URI_MATCHER.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.Movies.RESOURCE + "/#",
                MOVIES_OBJ
        )
    }

    private var dbHelper: DbOpenHelper? = null

    override fun onCreate(): Boolean {
        dbHelper = DbOpenHelper(context)
        return true
    }

    override fun getType(uri: Uri?): String {
        when (URI_MATCHER.match(uri)) {
            MOVIES_LST -> return MoviesContract.Movies.CONTENT_TYPE
            MOVIES_OBJ -> return MoviesContract.Movies.CONTENT_ITEM_TYPE
            else -> throw badUri(uri)
        }
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        var sortOrder = sortOrder

        val qbuilder = SQLiteQueryBuilder()
        when (URI_MATCHER.match(uri)) {
            MOVIES_LST -> {
                qbuilder.tables = DbSchema.Movies.TBL_NAME
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = MoviesContract.Movies.DEFAULT_SORT_ORDER
                }
            }
            MOVIES_OBJ -> {
                qbuilder.tables = DbSchema.Movies.TBL_NAME
                qbuilder.appendWhere(DbSchema.COL_ID + "=" + uri!!.lastPathSegment)
            }
            else -> throw badUri(uri)
        }

        val db = dbHelper!!.readableDatabase
        val cursor = qbuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        return cursor
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        val table : String
        when (URI_MATCHER.match(uri)) {
            MOVIES_LST -> table = DbSchema.Movies.TBL_NAME
            else -> throw badUri(uri)
        }

        val db = dbHelper!!.writableDatabase
        val newId = db.insert(table, null, values)

        return ContentUris.withAppendedId(uri, newId)
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper!!.writableDatabase
        var count: Int
        var where: String = selection ?: ""
        when (URI_MATCHER.match(uri)) {
            MOVIES_LST -> { }
        // If URI is main table, update uses incoming where clause and args.

            MOVIES_OBJ -> {
                // If URI is for a particular row ID, update is based on incoming
                // data but modified to restrict to the given ID.
                where = DbSchema.COL_ID + " = " + ContentUris.parseId(uri)

            }
            else -> throw IllegalArgumentException("Unknown URI " + uri)
        }

        count = db.update(DbSchema.Movies.TBL_NAME, values, where, selectionArgs)

        return count
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        val table : String
        when (URI_MATCHER.match(uri)) {
            MOVIES_LST -> {
                table = DbSchema.Movies.TBL_NAME
                if (selection != null) {
                    throw IllegalArgumentException("selection not supported")
                }
            }
            else -> throw badUri(uri)
        }

        val db = dbHelper!!.writableDatabase
        var ndel = db.delete(table, selection, selectionArgs)

        return ndel
    }

    private fun badUri(uri: Uri?) =
            IllegalArgumentException("unknown uri: $uri");
}