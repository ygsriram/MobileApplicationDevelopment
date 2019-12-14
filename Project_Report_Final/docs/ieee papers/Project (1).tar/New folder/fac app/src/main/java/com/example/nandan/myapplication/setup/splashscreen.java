package com.example.nandan.myapplication.setup;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.faculty.MainActivity;
import com.example.nandan.myapplication.faculty.SessionManager;

/**
 * Created by nandan on 22/5/16.
 */
public class splashscreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        session = new SessionManager(getApplicationContext());
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                if (session.isLoggedIn()) {
                    // User is already logged in. Take him to main activity
                    Intent intent = new Intent(splashscreen.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();
                }else {
                    Intent intent = new Intent(splashscreen.this, getstarted.class);
                    String transitionName = getString(R.string.commonname1);
                    View viewStart = findViewById(R.id.imageView2);
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(splashscreen.this, viewStart, transitionName);
                    startActivity(intent, transitionActivityOptions.toBundle());
                    finish();
                }

                // close this activity

            }
        }, SPLASH_TIME_OUT);
    }

}
