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
    private static final String SUB = "subject_detail";
    private static final String TEMP12 = "temp_rec";
    // Login Table Columns names
    private static final String KEY_SID = "Sid";
    private static final String KEY_NAME = "Name";
    private static final String KEY_SEM = "Semester";
    private static final String KEY_SEC = "Section";
    private static final String KEY_DEPT = "Department";
    private static final String KEY_EMAIL = "Email_ID";
    private static final String KEY_MOBNO = "Mobile_number";
    private static final String KEY_SUB = "sub";
    private static final String KEY_SUBCODE = "subcode";
    private static final String KEY_FID = "fid";
    private static final String KEY_SUBNAME = "subname";
    private static final String KEY_FNAME = "fname";

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
        String CREATE_SUBJECT_TABLE = "CREATE TABLE " + SUB + "("
                + KEY_SUBCODE + " TEXT PRIMARY KEY," + KEY_SUB + " TEXT" + ")";
        db1.execSQL(CREATE_SUBJECT_TABLE);
        String CREATE_TEMP_TABLE = "CREATE TABLE " + TEMP12 + "("
                + KEY_SUBCODE + " TEXT," + KEY_FID + " TEXT," + KEY_SUBNAME + " TEXT," + KEY_FNAME + " TEXT" + ")";
        db1.execSQL(CREATE_TEMP_TABLE);
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
    public void addSubject(String subcode,String subject) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put(KEY_SUBCODE, subcode);
        values1.put(KEY_SUB, subject);
        long id1 = db1.insert(SUB, null, values1);
        db1.close(); // Closing database connection

        Log.d(TAG, "Subject inserted into sqlite: " + id1);

    }
    public void addtempvalues(String subcode,String fid,String subname,String fname) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put(KEY_SUBCODE, subcode);
        values1.put(KEY_FID, fid);
        values1.put(KEY_SUBNAME, subname);
        values1.put(KEY_FNAME, fname);
        long id1 = db1.insert(TEMP12, null, values1);
        db1.close(); // Closing database connection

        Log.d(TAG, "Temp values inserted inserted into sqlite: " + id1);

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
    public HashMap<String, String> getSubjectDetails1() {
        HashMap<String, String> user1 = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + SUB;
        int i=0;
        int k=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        do{
            user1.put("subcode" + i, cursor.getString(0));
            user1.put("subject" + i, cursor.getString(1));
            i++;
            }while(cursor.moveToNext());
        cursor.close();
        db.close();
        user1.put("entry",""+i);
        // return user1
        Log.d(TAG, "Fetching subject from Sqlite: " + user1.toString());
        return user1;
    }
    public HashMap<String, String> gettempDetails1() {
        HashMap<String, String> user1 = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TEMP12;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            user1.put("subcode", cursor.getString(0));
            user1.put("fid", cursor.getString(1));
            user1.put("subname", cursor.getString(2));
            user1.put("fname", cursor.getString(3));

        }
        cursor.close();
        db.close();
        // return user1
        Log.d(TAG, "Fetching temp values from Sqlite: " + user1.toString());
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
    public void deletetemp() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TEMP12, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


}