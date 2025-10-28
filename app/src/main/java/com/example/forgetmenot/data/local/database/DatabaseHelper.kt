package com.example.forgetmenot.data.local.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import androidx.annotation.Nullable
import android.os.Looper
import android.util.Log
import android.os.Handler

class DatabaseHelper(private val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "Storage.db"
        const val DATABASE_VERSION = 2
        const val TABLE_NAME = "item"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_DATE_ACQUISITION = "date"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_CONDITION = "condition"
        const val COLUMN_PRICE = "price"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_TAGS = "tags"

        private const val TAG = "DatabaseHelper"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_CATEGORY TEXT, " +
                "$COLUMN_DATE_ACQUISITION DATE, " +
                "$COLUMN_LOCATION TEXT, " +
                "$COLUMN_CONDITION TEXT," +
                "$COLUMN_PRICE REAL, " +
                "$COLUMN_IMAGE_URL TEXT, " +
                "$COLUMN_TAGS TEXT);"

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addItem(
        name: String,
        description: String,
        category: String,
        date: String,
        location: String,
        condition: String,
        price: Double,
        imageUrl: String?,
        tags: String
    ) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_NAME, name)
        cv.put(COLUMN_DESCRIPTION, description)
        cv.put(COLUMN_CATEGORY, category)
        cv.put(COLUMN_DATE_ACQUISITION, date)
        cv.put(COLUMN_LOCATION, location)
        cv.put(COLUMN_CONDITION, condition)
        cv.put(COLUMN_PRICE, price)
        cv.put(COLUMN_IMAGE_URL, imageUrl)

        val result = db.insert(TABLE_NAME, null, cv)

        val message = if (result == -1L) {
            Log.e(TAG, "Fallo en guardar dato")
            "Fallo en guardar dato"
        } else {
            Log.i(TAG, "Se agregó exitosamente")
            "Se agregó exitosamente"
        }

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun readAllData(): Cursor? {
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase

        var cursor: Cursor? = null

        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun updateData(
        rowId: String,
        name: String,
        description: String,
        category: String,
        location: String,
        date: String,
        condition: String,
        price: Double,
        imageUrl: String?,
        tags: String
    ) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_NAME, name)
        cv.put(COLUMN_DESCRIPTION, description)
        cv.put(COLUMN_CATEGORY, category)
        cv.put(COLUMN_LOCATION, location)
        cv.put(COLUMN_DATE_ACQUISITION, date)
        cv.put(COLUMN_CONDITION, condition)
        cv.put(COLUMN_PRICE, price)
        cv.put(COLUMN_IMAGE_URL, imageUrl)
        cv.put(COLUMN_TAGS, tags)

        val result = db.update(
            TABLE_NAME,
            cv,
            "$COLUMN_ID = ?",
            arrayOf(rowId)
        )

        val message = when {
            result == -1 -> {
                Log.e(TAG, "Fallo en actualizar (ID: $rowId)")
                "Fallo en actualizar"
            }
            result == 0 -> {
                Log.w(TAG, "No se encontró el item para actualizar o no hubo cambios (ID: $rowId)")
                "No se encontró el item para actualizar"
            }
            else -> {
                Log.i(TAG, "Actualizado exitosamente (ID: $rowId)")
                "Actualizado exitosamente"
            }
        }

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteData(rowId: String) {
        val db = this.writableDatabase

        val result = db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(rowId)
        )

        val message = when {
            result == 0 -> {
                Log.w(TAG, "No se encontró el item para borrar (ID: $rowId)")
                "No se encontró el item para borrar"
            }
            result > 0 -> {
                Log.i(TAG, "Borrado exitosamente (ID: $rowId, rows: $result)")
                "Borrado exitosamente"
            }
            else -> {
                Log.e(TAG, "Error desconocido al borrar (ID: $rowId, result: $result)")
                "Error al borrar"
            }
        }

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
