package com.example.nandan.myapplication.faculty;

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
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "project";

    // Login table name
    private static final String TABLE_USER = "faculty_details";
    private static final String TABLE_SUBJECT = "sub_handled";

    // Login Table Columns names
    private static final String KEY_FID = "Fid";
    private static final String KEY_NAME = "Name";
    private static final String KEY_DEPT = "Department";
    private static final String KEY_EMAIL = "Email_ID";
    private static final String KEY_MOBNO = "Mobile_number";
    private static final String KEY_SUB = "subcode";
    private static final String KEY_SUBNAME = "subname";
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_FID + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DEPT + " TEXT," + KEY_EMAIL + " TEXT," + KEY_MOBNO + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        String CREATE_SUB = "CREATE TABLE " + TABLE_SUBJECT + "("
                + KEY_SUB + " TEXT PRIMARY KEY," + KEY_SUBNAME + " TEXT" + ")";
        db.execSQL(CREATE_SUB);
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
    public void addUser(String fid, String name, String dept, String email, String mobno) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FID, fid); //Student ID
        values.put(KEY_NAME, name); //Name
        values.put(KEY_DEPT, dept); // Department
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_MOBNO, mobno); // Mobile Number

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    public void addsub(String subcode,String subject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SUB, subcode);
        values.put(KEY_SUBNAME, subject);
        Log.d("values12 ",""+subcode+" "+subject);
        // Inserting Row
        long id = db.insert(TABLE_SUBJECT, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "New subject inserted into sqlite: " + id);
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
            user.put("fid", cursor.getString(0));
            user.put("name", cursor.getString(1));
            user.put("dept", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("mobno", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
        return user;
    }
    public HashMap<String, String> getSubject() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_SUBJECT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        int i=0;
        do{
            user.put("subcode"+i, cursor.getString(1));
            user.put("subname"+i, cursor.getString(0));
            i++;
        }while(cursor.moveToNext());
        cursor.close();
        user.put("entry",""+i);
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