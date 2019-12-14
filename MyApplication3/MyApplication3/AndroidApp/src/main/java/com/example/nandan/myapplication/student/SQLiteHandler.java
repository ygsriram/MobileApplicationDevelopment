package com.example.nandan.myapplication.student;

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
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "project";

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
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_SID + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SEM + " INTEGER," + KEY_SEC + " TEXT,"
                + KEY_DEPT + " TEXT," + KEY_EMAIL + " TEXT," + KEY_MOBNO + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String sid, String name, int sem, String sec, String dept, String email, String mobno) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SID, sid); //Student ID
        values.put(KEY_NAME, name); //Name
        values.put(KEY_SEM, sem); //Semester
        values.put(KEY_SEC, sec); // Section
        values.put(KEY_DEPT, dept); // Department
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_MOBNO, mobno); // Mobile Number

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("sid", cursor.getString(0));
            user.put("name", cursor.getString(1));
            user.put("sem", cursor.getString(2));
            user.put("sec", cursor.getString(3));
            user.put("dept", cursor.getString(4));
            user.put("email", cursor.getString(5));
            user.put("mobno", cursor.getString(6));


        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
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