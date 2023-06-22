package com.example.temp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "instruction_manuals.db"
        private const val DATABASE_VERSION = 1

        // Table names
        private const val TABLE_MANUALS = "manuals"
        private const val TABLE_PAGES = "pages"

        // Common columns
        private const val COLUMN_ID = "id"
        private const val COLUMN_IMAGE_PATH = "image_path"

        // Manuals table columns
        private const val COLUMN_MANUAL_NAME = "manual_name"

        // Pages table columns
        private const val COLUMN_ID_MANUAL = "idManual"
        private const val COLUMN_PAGE_NUMBER = "page_number"
        private const val COLUMN_PAGE_NAME = "page_name"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create the manuals table
        val createManualsTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_MANUALS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_MANUAL_NAME TEXT, $COLUMN_IMAGE_PATH TEXT)"
        db?.execSQL(createManualsTableQuery)

        // Create the pages table
        val createPagesTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_PAGES ($COLUMN_ID_MANUAL INTEGER, $COLUMN_PAGE_NUMBER INTEGER, $COLUMN_PAGE_NAME TEXT, $COLUMN_IMAGE_PATH TEXT, FOREIGN KEY ($COLUMN_ID_MANUAL) REFERENCES $TABLE_MANUALS($COLUMN_ID))"
        db?.execSQL(createPagesTableQuery)

        // Insert sample data into manuals table
        insertManual(db, "Manual 1", "poule")
        insertManual(db, "Manual 2", "etape2")
        insertManual(db, "Manual 3", "etape3")

        // Insert sample data into pages table
        insertPage(db, 1, 1, "Etape 1", "etape1")
        insertPage(db, 1, 2, "Etape 2", "etape2")
        insertPage(db, 1, 3, "Etape 3", "etape3")
        insertPage(db, 1, 4, "Etape 4", "etape4")
        insertPage(db, 1, 5, "Etape 5", "etape5")
        insertPage(db, 1, 6, "Etape 6", "etape6")
        insertPage(db, 1, 7, "Etape 7", "etape7")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
    }

    private fun insertManual(db: SQLiteDatabase?, manualName: String, imagePath: String) {
        val values = ContentValues().apply {
            put(COLUMN_MANUAL_NAME, manualName)
            put(COLUMN_IMAGE_PATH, imagePath)
        }
        db?.insert(TABLE_MANUALS, null, values)
    }

    private fun insertPage(db: SQLiteDatabase?, idManual: Int, pageNumber: Int, pageName: String, imagePath: String) {
        val values = ContentValues().apply {
            put(COLUMN_ID_MANUAL, idManual)
            put(COLUMN_PAGE_NUMBER, pageNumber)
            put(COLUMN_PAGE_NAME, pageName)
            put(COLUMN_IMAGE_PATH, imagePath)
        }
        db?.insert(TABLE_PAGES, null, values)
    }

    fun getPagesForManual(manualId: Int): List<ListPage> {
        val db = readableDatabase
        val pages = mutableListOf<ListPage>()

        val query = "SELECT $COLUMN_PAGE_NUMBER, $COLUMN_IMAGE_PATH FROM $TABLE_PAGES WHERE $COLUMN_ID_MANUAL = ?"
        val selectionArgs = arrayOf(manualId.toString())

        val cursor = db.rawQuery(query, selectionArgs)
        cursor.use {
            val columnIndexPageNumber = cursor.getColumnIndex(COLUMN_PAGE_NUMBER)
            val columnIndexImagePath = cursor.getColumnIndex(COLUMN_IMAGE_PATH)
            while (cursor.moveToNext()) {
                if (columnIndexPageNumber != -1 && columnIndexImagePath != -1) {
                    val pageNumber = cursor.getInt(columnIndexPageNumber)
                    val imagePath = cursor.getString(columnIndexImagePath)
                    val listItem = ListPage(manualId, "Etape $pageNumber", imagePath)
                    pages.add(listItem)
                }
            }
        }

        return pages
    }

    fun getAllManuals(): List<List_Man> {
        val db = readableDatabase
        val manuals = mutableListOf<List_Man>()

        val query = "SELECT $COLUMN_ID, $COLUMN_MANUAL_NAME, $COLUMN_IMAGE_PATH FROM $TABLE_MANUALS"
        val cursor = db.rawQuery(query, null)

        cursor.use {
            val columnIndexId = cursor.getColumnIndex(COLUMN_ID)
            val columnIndexManualName = cursor.getColumnIndex(COLUMN_MANUAL_NAME)
            val columnIndexImagePath = cursor.getColumnIndex(COLUMN_IMAGE_PATH)

            while (cursor.moveToNext()) {
                if (columnIndexId != -1 && columnIndexManualName != -1 && columnIndexImagePath != -1) {
                    val manualId = cursor.getInt(columnIndexId)
                    val manualName = cursor.getString(columnIndexManualName)
                    val imagePath = cursor.getString(columnIndexImagePath)
                    val pages = getPagesForManual(manualId)
                    val listMan = List_Man(manualId, manualName, imagePath, pages)
                    manuals.add(listMan)
                }
            }
        }

        return manuals
    }
}
