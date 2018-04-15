package pt.isel.pdm.profslist.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.joao.yamda.provider.DbSchema

class DbOpenHelper(context: Context?) : SQLiteOpenHelper(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        deleteDb(db)
        createDb(db)
    }

    private fun createDb(db: SQLiteDatabase) {
        db.execSQL(DbSchema.Movies.DDL_CREATE_TABLE)
    }

    private fun deleteDb(db: SQLiteDatabase) {
        db.execSQL(DbSchema.Movies.DDL_DROP_TABLE)
    }

}