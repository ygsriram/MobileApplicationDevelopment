package com.example.nandan.myapplication.setup;

/**
 * Created by nandan on 12/3/16.
 */
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.faculty.MainActivity;
import com.example.nandan.myapplication.faculty.SQLiteHandler;
import com.example.nandan.myapplication.faculty.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Login extends Activity {

    private Button btnLogin;
    private EditText pwd;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private TextView tx1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Intent i = getIntent();
        final String fida = i.getStringExtra("fid");
        pwd = (EditText) findViewById(R.id.pwd1);
        btnLogin = (Button) findViewById(R.id.login);
       //tx1 = (TextView) findViewById(R.id.side);
        //tx1.setText("ID = " + fida);
        TextView tx2 = (TextView)findViewById(R.id.tooltext);
        tx2.setText("    "+fida);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());


        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String pwd1 = pwd.getText().toString().trim();
                String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Check for empty data in the form
                if (!pwd1.isEmpty()) {
                    // login user
                    checkLogin(fida,pwd1,id);

                } else {
                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Enter your password!")
                            .show();
                }
            }

        });

        // Link to Register Screen


    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String fida, final String pwd,final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_PWD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("login response", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String fid = jObj.getString("fid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("Name");

                        String dept = user.getString("Department");
                        String email = user.getString("Email_ID");
                        String mobno = user
                                .getString("Mobile_number");
                        JSONObject subject = jObj.getJSONObject("subject");
                        int i = subject.getInt("entry");
                        int j=0;
                        while(i!=j){
                            String subcode = subject.getString("subcode"+j);
                            String subject1 = subject.getString("sub"+j);
                            Log.d("values ",""+subcode+" "+subject1);
                            db.addsub(subcode,subject1);
                            j++;
                        }

                        // Inserting row in users table
                        db.addUser(fid, name, dept, email, mobno);

                        // Launch main activity
                        new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success Login")
                                .setContentText("Welcome Prof. "+ name)
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        String transitionName = getString(R.string.commonname);
                                        View viewStart = findViewById(R.id.login);
                                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(Login.this, viewStart, transitionName);
                                        startActivity(intent, transitionActivityOptions.toBundle());
                                        finish();
                                    }
                                })
                                .show();

                    } else {
                        // Error in login. Get the error message
                        new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Password is wrong!")
                                .show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("login error", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            int count=0;
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid", fida);
                params.put("password", pwd);
                params.put("deviceid",id);
               params.put("count", ""+count);
               count++;



                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
