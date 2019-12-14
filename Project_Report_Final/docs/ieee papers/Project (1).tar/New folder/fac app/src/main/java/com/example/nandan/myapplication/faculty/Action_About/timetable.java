package com.example.nandan.myapplication.faculty.Action_About;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.nandan.myapplication.R;

/**
 * Created by vigne on 14-04-2016.
 */
public class timetable extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstscreen);
        Intent intent =getIntent();
        Log.d("Timetable"," "+intent.getStringExtra("subcode"));
    }
}
