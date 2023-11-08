import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(private val context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                POSITION_COl + " TEXT," +
                TYPE_COL + " TEXT," +
                COST_COL + " REAL," +  // Добавляем поле COST
                COUNT_COL + " INTEGER," +
                RES_ID_COL + " INTEGER NULL" + ")")
        db.execSQL(query)
    }

    fun execute(db: SQLiteDatabase, query: String) {
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun loadFromResource(res: Int, clear: Boolean = false) {
        if (clear == true)
            clearTable()

        val db = this.writableDatabase
        val resources = context.resources.getStringArray(res)

        for (i in 0 until resources.size step 4){
            val position = resources[i]
            val drawable = resources[i + 1]
            var cost = resources[i + 2]
            val type = resources[i + 3]

            cost = cost.replace(',','.')

            var drawableID = context.resources.getIdentifier(drawable, "drawable", context.packageName)
            if (drawableID == 0)
                drawableID = context.resources.getIdentifier("sold", "drawable", context.packageName)

            addPosition(position, type, cost.toDouble(), 0, drawableID)
        }

        db.close()
    }

    fun clearTable() {
        onUpgrade(this.writableDatabase, DATABASE_VERSION, DATABASE_VERSION)
    }

    fun updateFieldForAll(fieldName: String, value: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(fieldName, value)
        db.update(TABLE_NAME, contentValues, null, null)
        db.close()
    }

    @SuppressLint("Range")
    fun getIDColValue(cursor: Cursor): Int {
        return cursor.getInt(cursor.getColumnIndex(ID_COL))
    }

    fun addPosition(position: String, type: String, cost: Double, count: Int, resId: Int?) {
        val values = ContentValues()
        values.put(POSITION_COl, position)
        values.put(TYPE_COL, type)
        values.put(COST_COL, cost)
        values.put(COUNT_COL, count)
        values.put(RES_ID_COL, resId)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getPositions(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    fun getPositionsByType(type: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $TYPE_COL = ?", arrayOf(type))
    }

    fun getRecordsWithCondition(field: String, condition: String, value: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $field $condition ?", arrayOf(value))
    }


    @SuppressLint("Range")
    fun searchByPosition(position: String): Long? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, arrayOf(ID_COL),
            "$POSITION_COl = ?",
            arrayOf(position),
            null, null, null
        )
        val id: Long? = if (cursor.moveToFirst()) {
            cursor.getLong(cursor.getColumnIndex(ID_COL))
        } else {
            null
        }
        cursor.close()
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun getPositionById(id: Int): PositionData? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, arrayOf(POSITION_COl, COST_COL, COUNT_COL, RES_ID_COL),
            "$ID_COL = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        val position: PositionData? = if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex(POSITION_COl))
            val type = cursor.getString(cursor.getColumnIndex(TYPE_COL))
            val cost = cursor.getDouble(cursor.getColumnIndex(COST_COL))
            val count = cursor.getInt(cursor.getColumnIndex(COUNT_COL))
            val resId = cursor.getInt(cursor.getColumnIndex(RES_ID_COL))
            PositionData(name, type, cost, count, resId)
        } else {
            null
        }
        cursor.close()
        db.close()
        return position
    }


    fun updateCountById(id: Int, newCount: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COUNT_COL, newCount)
        db.update(TABLE_NAME, values, "$ID_COL = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteRowById(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COL = ?", arrayOf(id.toString()))
        db.close()
    }

    companion object {
        private val DATABASE_NAME = "products_db"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "purchases_table"
        val ID_COL = "id"
        val POSITION_COl = "position"
        val TYPE_COL = "type"
        val COST_COL = "cost"
        val COUNT_COL = "count"
        val RES_ID_COL = "res_id"
    }

    data class PositionData(
        val position: String,
        val type: String,
        val cost: Double,
        val count: Int,
        val resId: Int?
    )
}
