package com.example.attendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "com.example.attendance.DatabaseHelper";
    private static final String DATABASE_NAME = "Attendance3";

    // course table
    private static final String COURSE_TABLE_NAME = "COURSE_TABLE";
    public static final String COURSE_ENTRY_ID = "_COURSE_ID";
    public static final String COURSE_NAME_KEY = "COURSE_NAME";
    public static final String COURSE_ID_KEY = "COURSE_ID";
    public static final String CREATE_COURSE_TABLE =
            String.format( "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "%s TEXT NOT NULL, %s TEXT NOT NULL, UNIQUE (%s,%s));" ,
                    COURSE_TABLE_NAME, COURSE_ENTRY_ID, COURSE_NAME_KEY, COURSE_ID_KEY,
                    COURSE_NAME_KEY, COURSE_ID_KEY);

    // student table
    private static final String STUDENT_TABLE_NAME = "STUDENT_TABLE";
    public static final String STUDENT_ENTRY_ID = "_STUDENT_ID";
    public static final String STUDENT_NAME_KEY = "STUDENT_NAME";
    public static final String STUDENT_ID_KEY = "STUDENT_ID";
    public static final String CREATE_STUDENT_TABLE =
            String.format( "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "%s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER, FOREIGN KEY (%s) " +
                            "REFERENCES %s(%s));" ,
                    STUDENT_TABLE_NAME, STUDENT_ENTRY_ID, COURSE_ID_KEY, STUDENT_NAME_KEY,
                    STUDENT_ID_KEY, COURSE_ID_KEY , COURSE_TABLE_NAME, COURSE_ID_KEY);


    // status table
    private static final String STATUS_TABLE_NAME = "STATUS_TABLE";
    public static final String STATUS_ENTRY_ID = "_STATUS_ID";
    public static final String DATE_KEY = "STATUS_DATE";
    public static final String STATUS_KEY = "STATUS";
    public static final String CREATE_STATUS_TABLE =
            String.format( "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "%s INTEGER, %s TEXT NOT NULL, %s DATE NOT NULL, %s TEXT NOT NULL, " +
                            "UNIQUE (%s,%s,%s), FOREIGN KEY (%s) REFERENCES %s(%s));" ,
                    STATUS_TABLE_NAME, STATUS_ENTRY_ID, STUDENT_ID_KEY, COURSE_ID_KEY, DATE_KEY,
                    STATUS_KEY, STUDENT_ID_KEY, DATE_KEY, COURSE_ID_KEY,
                    STUDENT_ID_KEY, STUDENT_TABLE_NAME, STUDENT_ID_KEY);



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STATUS_TABLE_NAME);


    }


    long addCourse(String name, String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COURSE_NAME_KEY, name);
        values.put(COURSE_ID_KEY, ID);

        return db.insert(COURSE_TABLE_NAME, null, values);
    }

    Cursor getCourses()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM " + COURSE_TABLE_NAME, null);
    }

    public int removeCourse(String courseID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.delete(COURSE_TABLE_NAME, COURSE_ID_KEY+"=?", new String[]{courseID});
    }


    public long addStudent(String courseID, int studentID, String studentName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COURSE_ID_KEY, courseID);
        values.put(STUDENT_ID_KEY, studentID);
        values.put(STUDENT_NAME_KEY, studentName);

        return db.insert(STUDENT_TABLE_NAME, null, values);
    }

    Cursor getStudents(String courseID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + STUDENT_TABLE_NAME + " WHERE "
                + COURSE_ID_KEY + "=" + courseID+ " ORDER BY " + STUDENT_ID_KEY;

        return db.rawQuery(query, null);
    }

    public int removeStudent(String studentID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.delete(STUDENT_TABLE_NAME, STUDENT_ID_KEY+"=?", new String[]{studentID});
    }

    public int updateStudent(int currID,String name, int newID){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME_KEY, name);
        values.put(STUDENT_ID_KEY, newID);
        return database.update(STUDENT_TABLE_NAME,values,STUDENT_ID_KEY+"=?",new String[]{String.valueOf(currID)});
    }

    long addStatus(int studentID, String courseID, String date, String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(STUDENT_ID_KEY, studentID);
        values.put(COURSE_ID_KEY, courseID);
        values.put(DATE_KEY, date);
        values.put(STATUS_KEY, status);

        return db.insert(STATUS_TABLE_NAME, null, values);
    }

    long updateStatus(int studentID, String courseID, String date, String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(STATUS_KEY, status);

        String clause = DATE_KEY + "=" + date + " AND " + STUDENT_ID_KEY + "=" + studentID +
                " AND " + COURSE_ID_KEY + "=" + courseID;

        return db.update(STATUS_TABLE_NAME, values, clause, null);
    }

    String getStatus(int studentID, String courseID, String date)
    {
        String status = "Present";
        SQLiteDatabase db = this.getReadableDatabase();

        String clause = DATE_KEY + "=" + date + " AND " + STUDENT_ID_KEY + "=" + studentID +
                " AND " + COURSE_ID_KEY + "=" + courseID;

        Cursor crs = db.query(STATUS_TABLE_NAME, null, clause, null, null, null, null);

        if (crs.moveToFirst())
        {
            status = crs.getString(crs.getColumnIndexOrThrow(STATUS_KEY));
            Log.d("STATUS", status);
        }
        return status;
    }



//    public boolean addData(String item)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL1, item);
//        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);
//
//        long result = db.insert(TABLE_NAME, null, contentValues);
//
//        return result != -1;
//    }
//
//
//    public Cursor getData()
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_NAME;
//        Cursor data = db.rawQuery(query, null);
//
//        return data;
//    }


}
