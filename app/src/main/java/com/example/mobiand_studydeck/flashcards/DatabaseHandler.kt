package com.example.mobiand_studydeck.flashcards

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "FlashcardsDB"
        private const val TABLE_FLASHCARDS = "FlashcardsTB"

        private const val KEY_ID = "_id"
        private const val KEY_TERM = "term"
        private const val KEY_DEFINITION = "definition"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_FLASHCARDS_TABLE = ("CREATE TABLE " + TABLE_FLASHCARDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TERM + " TEXT,"
                + KEY_DEFINITION + " TEXT" + ")")
        db?.execSQL(CREATE_FLASHCARDS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_FLASHCARDS")
        onCreate(db)
    }

    /**
     * Function to insert data
     */
    fun addFlashcard(card: FlashcardsModelClass): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TERM, card.term) // EmpModelClass Name
        contentValues.put(KEY_DEFINITION, card.definition) // EmpModelClass Email

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_FLASHCARDS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    //Method to read the records from database in form of ArrayList
    fun viewFlashcard(): ArrayList<FlashcardsModelClass> {

        val cardList: ArrayList<FlashcardsModelClass> = ArrayList<FlashcardsModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT * FROM $TABLE_FLASHCARDS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var term: String
        var definition: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                term = cursor.getString(cursor.getColumnIndex(KEY_TERM))
                definition = cursor.getString(cursor.getColumnIndex(KEY_DEFINITION))

                val card = FlashcardsModelClass(id = id, term = term, definition = definition)
                cardList.add(card)

            } while (cursor.moveToNext())
        }
        return cardList
    }

    /**
     * Function to update record
     */
    fun updateFlashcard(flashcard: FlashcardsModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TERM, flashcard.term) // Changing Term
        contentValues.put(KEY_DEFINITION, flashcard.definition) // Changing Definition

        val success = db.update(TABLE_FLASHCARDS, contentValues, KEY_ID + "=" + flashcard.id, null)

        db.close()
        return success
    }

    /**
     * Function to delete record
     */
    fun deleteFlashcard(flashcard: FlashcardsModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, flashcard.id) // EmpModelClass id
        // Deleting Row
        val success = db.delete(TABLE_FLASHCARDS, KEY_ID + "=" + flashcard.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }
}