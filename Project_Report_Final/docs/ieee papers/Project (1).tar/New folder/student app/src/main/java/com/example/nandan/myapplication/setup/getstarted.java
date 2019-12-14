package com.example.nandan.myapplication.setup;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.student.GUI.studentMainActivity;
import com.example.nandan.myapplication.student.setup.SessionManager;

/**
 * Created by nandan on 15/5/16.
 */
public class getstarted extends AppCompatActivity {
    private SessionManager session;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstscreen);
        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(getstarted.this, studentMainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        }
    }
    public void start(View V){
        Intent intent = new Intent(getstarted.this,id_validation.class);
        String transitionName = getString(R.string.commonname);
        View viewStart = findViewById(R.id.button1);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getstarted.this, viewStart, transitionName);
        startActivity(intent, transitionActivityOptions.toBundle());
    }
}
