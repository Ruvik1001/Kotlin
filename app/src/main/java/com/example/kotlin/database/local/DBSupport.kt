package com.example.kotlin.database.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class DBSupport(private val context: Context) {

    private val dbName = "LocalDB"
    private val dbVersion = 1
    private var currentTableName: String = ""

    private inner class DBHelper(context: Context, val filed: List<Pair<String, String>>) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
        override fun onCreate(db: SQLiteDatabase) {
            var command = "CREATE TABLE IF NOT EXISTS $currentTableName (id INTEGER PRIMARY KEY AUTOINCREMENT"
            for (pair in filed) {
                command += ", " + pair.first + " " + pair.second
            }
            command += ")"
            db.execSQL(command)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $currentTableName")
            onCreate(db)
        }
    }

    private var dbHelper: DBHelper? = null
    private var database: SQLiteDatabase? = null

    fun selectTable(tableName: String, filed: List<Pair<String, String>>) {
        currentTableName = tableName
        dbHelper = DBHelper(context, filed)
        database = dbHelper!!.writableDatabase
    }

    fun clearSelectedTable() {
        val db = dbHelper?.writableDatabase
        if (db != null) {
            dbHelper?.onUpgrade(db, 1, 1)
        }
    }

    fun recordExists(columnName: String, valueToCheck: String): Boolean {
        val query = "SELECT $columnName FROM $currentTableName WHERE $columnName = ?"
        val cursor = database?.rawQuery(query, arrayOf(valueToCheck))

        cursor?.use {
            if (it.moveToFirst())
                return it.count > 0
        }

        return false
    }

    fun deleteRecordsByColumnValue(columnName: String, value: String, operator: String = "=") {
        val whereClause = "$columnName " + operator + " ?"
        val whereArgs = arrayOf(value)

        database?.delete(currentTableName, whereClause, whereArgs)
    }

    fun getAllDataFromCurrentTable(): List<List<String>> {
        val data = mutableListOf<List<String>>()
        val cursor = database?.rawQuery("SELECT * FROM $currentTableName;", null)
        cursor?.use {
            val columnCount = cursor.columnCount
            while (it.moveToNext()) {
                val rowData = mutableListOf<String>()
                for (i in 0 until columnCount) {
                    val value = it.getString(i)
                    rowData.add(value)
                }
                data.add(rowData)
            }
        }
        return data
    }


    fun addDataToCurrentTable(data: List<Pair<String, String>>) {
        val values = ContentValues()
        for (item in data) {
            values.put(item.first, item.second)
        }
        database?.insert(currentTableName, null, values)
    }

    fun parseToXmlFileAll(filePath: String) {
        val data = getAllDataFromCurrentTable()
        val xmlSerializer = Xml.newSerializer()

        val file = File(context.getExternalFilesDir(null), filePath)
        val fileOutputStream = FileOutputStream(file)
        val outputStreamWriter = OutputStreamWriter(fileOutputStream)

        xmlSerializer.setOutput(outputStreamWriter)

        xmlSerializer.startDocument("UTF-8", true)
        xmlSerializer.startTag(null, "data")

        for (rowData in data) {
            xmlSerializer.startTag(null, "row")
            for (columnValue in rowData) {
                xmlSerializer.startTag(null, "column")
                xmlSerializer.text(columnValue)
                xmlSerializer.endTag(null, "column")
            }
            xmlSerializer.endTag(null, "row")
        }

        xmlSerializer.endTag(null, "data")
        xmlSerializer.endDocument()

        outputStreamWriter.close()
        fileOutputStream.close()
    }

    fun parseFromXmlFileAll(filePath: String) {
        val file = File(context.getExternalFilesDir(null), filePath)
        val fileInputStream = FileInputStream(file)

        val parserFactory = XmlPullParserFactory.newInstance()
        val parser = parserFactory.newPullParser()

        parser.setInput(fileInputStream, null)

        var eventType = parser.eventType
        var currentRow: MutableList<Pair<String, String>>? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "row" -> currentRow = mutableListOf()
                        "column" -> currentRow?.add(Pair("column_name", parser.nextText()))
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "row" && currentRow != null) {
                        addDataToCurrentTable(currentRow)
                    }
                }
            }
            eventType = parser.next()
        }

        fileInputStream.close()
    }

    fun loadFromResourceArray(resId: Int, columnsCount: Int, clear: Boolean = false) {
        if (clear) {
            clearSelectedTable()
        }

        val parser = context.resources.getXml(resId)
        var eventType = parser.eventType

        val lst = mutableListOf<Pair<String, String>>()

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (parser.name == "item") {
                        for (i in 0 until columnsCount) {
                            lst.add(Pair<String, String>(parser.nextText(),parser.nextText()))
                        }
                        addDataToCurrentTable(lst)
                        lst.clear()
                    }
                }
            }
            eventType = parser.next()
        }
    }

}
