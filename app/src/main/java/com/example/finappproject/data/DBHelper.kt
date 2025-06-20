package com.example.finappproject.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_NAME = "finapp.db"
            private const val DATABASE_VERSION = 3
            private const val TABLE_NAME = "transactions"
        }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "operation TEXT, " +
                "description TEXT, " +
                "value REAL, " +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertTransaction(operation: String, description: String, value: Double) {
        val db = writableDatabase
        val register = ContentValues().apply {
            put("operation", operation)
            put("description", description)
            put("value", value)
            put("timestamp", System.currentTimeMillis())
        }
        db.insert(TABLE_NAME, null, register)
        db.close()
    }

    fun consultBalance(): Double {
        val db = readableDatabase
        db.rawQuery("SELECT SUM(value) FROM $TABLE_NAME", null).use { cursor ->
            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                return cursor.getDouble(0)
            }
        }
        return 0.0
    }

    fun consultStatement(operation: String): List<Transaction> {
        val db = readableDatabase
        var filter: String? = null
        var operationType: Array<String>? = null

        if (operation != "Todos") {
            filter = "operation = ?"
            operationType = arrayOf(operation)
        }

        val cursor = db.query(TABLE_NAME, arrayOf("id", "operation", "description", "value", "timestamp"),
            filter, operationType, null, null, "timestamp DESC")

        val transactionList = mutableListOf<Transaction>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val operation = getString(getColumnIndexOrThrow("operation"))
                val description = getString(getColumnIndexOrThrow("description"))
                val value = getDouble(getColumnIndexOrThrow("value"))
                val timestamp = getLong(getColumnIndexOrThrow("timestamp"))
                val transaction = Transaction(id, operation, description, value, timestamp)
                transactionList.add(transaction)
            }
        }
        cursor.close()
        db.close()
        Log.d("Transactions db helper", transactionList.toString())
        return transactionList
    }

}