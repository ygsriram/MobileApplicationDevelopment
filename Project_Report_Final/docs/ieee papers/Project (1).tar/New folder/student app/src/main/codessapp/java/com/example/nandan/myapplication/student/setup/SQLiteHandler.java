package com.example.nandan.myapplication.student.setup;

/**
 * Created by nandan on 12/3/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "project1";

    // Login table name
    private static final String TABLE_USER = "student_details";

    // Login Table Columns names
    private static final String KEY_SID = "Sid";
    private static final String KEY_NAME = "Name";
    private static final String KEY_SEM = "Semester";
    private static final String KEY_SEC = "Section";
    private static final String KEY_DEPT = "Department";
    private static final String KEY_EMAIL = "Email_ID";
    private static final String KEY_MOBNO = "Mobile_number";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db1) {
        String CREATE_LOGIN_TABLE1 = "CREATE TABLE " + TABLE_USER + "("
                + KEY_SID + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SEM + " INTEGER," + KEY_SEC + " TEXT,"
                + KEY_DEPT + " TEXT," + KEY_EMAIL + " TEXT," + KEY_MOBNO + " TEXT" + ")";
        db1.execSQL(CREATE_LOGIN_TABLE1);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db1, int oldVersion, int newVersion) {
        // Drop older table if existed
        db1.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db1);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String sid, String name, int sem, String sec, String dept, String email, String mobno) {
        SQLiteDatabase db1 = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(KEY_SID, sid); //Student ID
        values1.put(KEY_NAME, name); //Name
        values1.put(KEY_SEM, sem); //Semester
        values1.put(KEY_SEC, sec); // Section
        values1.put(KEY_DEPT, dept); // Department
        values1.put(KEY_EMAIL, email); // Email
        values1.put(KEY_MOBNO, mobno); // Mobile Number

        // Inserting Row
        long id1 = db1.insert(TABLE_USER, null, values1);
        db1.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id1);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails1() {
        HashMap<String, String> user1 = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user1.put("sid", cursor.getString(0));
            user1.put("name", cursor.getString(1));
            user1.put("sem", cursor.getString(2));
            user1.put("sec", cursor.getString(3));
            user1.put("dept", cursor.getString(4));
            user1.put("email", cursor.getString(5));
            user1.put("mobno", cursor.getString(6));


        }
        cursor.close();
        db.close();
        // return user1
        Log.d(TAG, "Fetching user from Sqlite: " + user1.toString());

        return user1;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}