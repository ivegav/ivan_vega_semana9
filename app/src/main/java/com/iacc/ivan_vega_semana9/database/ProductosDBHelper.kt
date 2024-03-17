package com.iacc.ivan_vega_semana9.database

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProductosDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "productos.db"
        internal const val TABLE_NAME = "productos"
        internal const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_RATE = "rate"
        private const val COLUMN_COUNT = "count"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_TITLE TEXT,"
                + "$COLUMN_PRICE TEXT,"
                + "$COLUMN_DESCRIPTION TEXT,"
                + "$COLUMN_CATEGORY TEXT"
                + "$COLUMN_IMAGE TEXT"
                + "$COLUMN_RATE TEXT"
                + "$COLUMN_COUNT TEXT"
                + ")")
        db.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}