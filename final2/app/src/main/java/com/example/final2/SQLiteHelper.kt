package com.example.final2


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class SQLiteHelper(context: Context):
    SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object {
        // Database version, name, table name and columns are defined here as constants.
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "student.db"
        private const val TBL_STUDENT = "tbl_student"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PHONE = "phone"
        private const val PASS = "pass"

    }
    // creation of tables
    override fun onCreate(db: SQLiteDatabase?) {
        val createTblStudent = ("CREATE TABLE " + TBL_STUDENT + "("
                + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, "
                + EMAIL + " TEXT, " + PHONE + " TEXT, " + PASS + " TEXT)")
        db?.execSQL(createTblStudent)
    }
    // Called when the database needs to be upgraded. This method will only be called if a database already exists
    // on disk with the same DATABASE_NAME, but the DATABASE_VERSION is lower than the version of the current application.
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT")
        onCreate(db)
    }
    // Insert Student data into SQLite
    fun insertStudent(std: StudentModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(EMAIL, std.email)
        contentValues.put(PHONE, std.phone)
        contentValues.put(PASS, std.pass)

        val success = db.insert(TBL_STUDENT, null, contentValues)
        db.close()
        return success
    }
    // Get all Students data from SQLite
    fun getAllStudent(): ArrayList<StudentModel> {
        // A new ArrayList of StudentModel is created to store the students fetched from the database.
        val stdList: ArrayList<StudentModel> = ArrayList<StudentModel>()
        //SELECT QUERY - fetch all rows from the student table.
        val selectQuery = "SELECT * FROM $TBL_STUDENT"
        // A readable database instance
        val db = this.readableDatabase
        // Cursor is used to read the result set returned
        val cursor: Cursor?

        try {
            // executes the SQL query and returns the result set.
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            // If an exception is thrown during the query execution,the stack trace is printed if caught
            e.printStackTrace()
            // The SELECT query is executed again
            db.execSQL(selectQuery)
            // If an error occurs, an empty ArrayList is returned.
            return ArrayList()
        }
        // These variables will be used to temporarily hold the data of each student.
        var id: Int
        var name: String
        var email: String
        var phone: String
        var pass: String

        //function is used to move the cursor to the first row in the result set.
        if (cursor.moveToFirst()) {
            // loop, we traverse each row in the result set.
            do {
                try {
                    // Get the data for each student from the cursor using column names.
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                    val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                    val pass = cursor.getString(cursor.getColumnIndexOrThrow("pass"))

                    //Create a StudentModel object with the data and add it to the ArrayList.
                    stdList.add(StudentModel(id, name, email, phone, pass))

                } catch (e: Exception) {
                    // If an exception is thrown during the data retrieval, it is caught here and the stack trace is printed.
                    e.printStackTrace()
                }
                // The loop continues moving to the next row until there are no more rows in the result set.
            } while (cursor.moveToNext())
        }
        // Close the cursor and database when you're finished with them.
        cursor.close()
        db.close()
        // Return the ArrayList with the students.
        return stdList
    }
    // Update Student data in SQLite
    fun updateStudent(std: StudentModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(EMAIL, std.email)
        contentValues.put(PHONE, std.phone)
        contentValues.put(PASS, std.pass)

        val success = db.update(TBL_STUDENT, contentValues, "id=" + std.id, null)
        db.close()
        return success
    }
    // Delete Student data from SQLite by ID
    fun deleteStudentById(id: Int): Int{
        val db= this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, id)
        val success = db.delete(TBL_STUDENT,"id=$id",null)
        db.close()
        return success
    }
}

