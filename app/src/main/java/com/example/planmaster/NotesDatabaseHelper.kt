package com.example.planmaster

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotesDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = (
                "CREATE TABLE " + TABLE_NOTES + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_TITLE + " TEXT," +
                        COLUMN_CONTENT + " TEXT" + ")"
                )
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    fun addMyNote(note: Note): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, note.title)
        contentValues.put(COLUMN_CONTENT, note.content)

        val success = db.insert(TABLE_NOTES, null, contentValues)
        db.close()
        return success
    }

    fun updateMyNote(note: Note): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, note.title)
        contentValues.put(COLUMN_CONTENT, note.content)

        val success = db.update(TABLE_NOTES, contentValues, "$COLUMN_ID=?", arrayOf(note.id.toString()))
        db.close()
        return success
    }

    fun deleteMyNote(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NOTES, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }

    fun getAllNotes(): List<Note> {
        val noteList = ArrayList<Note>()
        val selectQuery = "SELECT  * FROM $TABLE_NOTES"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

                val note = Note(
                    id = id,
                    title = title,
                    content = content
                )
                noteList.add(note)
            }
            cursor.close()
        }
        db.close()
        return noteList
    }



    fun getNoteByID(id: Int): Note {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NOTES,
            arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT),
            "$COLUMN_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()

        val note = if (cursor != null && cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

            Note(
                id = id,
                title = title,
                content = content
            )
        } else {
            Note(
                id = -1,
                title = "",
                content = ""
            )
        }
        cursor?.close()
        db.close()
        return note
    }



    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MyNotesDB"
        private const val TABLE_NOTES = "MyNotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }
}
