package com.example.nandan.myapplication.setup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.student.GUI.studentMainActivity;
import com.example.nandan.myapplication.student.setup.SQLiteHandler;
import com.example.nandan.myapplication.student.setup.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nandan on 16/3/16.
 */
public class id_validation extends AppCompatActivity {
    private Button btnLogin;
    private EditText sid;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sid_validate);

        sid = (EditText) findViewById(R.id.sid);
        btnLogin = (Button) findViewById(R.id.next);
        String sid1=sid.getText().toString();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity

            Intent intent = new Intent(id_validation.this, studentMainActivity.class);
            intent.putExtra("Sid",sid1);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String sida = sid.getText().toString().trim();
                String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Check for empty data in the form
                if (!sida.isEmpty()) {
                    // login user
                    checkSid(sida, id);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen


    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkSid(final String sida,final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_sid";

        pDialog.setMessage("Sid check ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SIDVALIDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Sid response", "Sid Response: " + response.toString() + " ID =  " + id);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user sid validated
                        /*String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();*/
                        if(jObj.getInt("ide")==1) {
                            Intent intent = new Intent(id_validation.this, Login.class);
                            intent.putExtra("sid", sida);
                            startActivity(intent);
                            finish();
                        }
                       else if (jObj.getString("deviceid")==id)
                        {
                            session.setLogin(true);
                            String sid = jObj.getString("Sid");
                            Log.d("Sid response", "Device ID Response: " + response.toString());
                            JSONObject user1 = jObj.getJSONObject("user");
                            String name = user1.getString("Name");
                            int sem = user1.getInt("Semester");
                            String sec = user1.getString("Section");
                            String dept = user1.getString("Department");
                            String email = user1.getString("Email_ID");
                            String mobno = user1.getString("Mobile_number");

                            // Inserting row in users table
                            db.addUser(sid, name, sem, sec, dept, email, mobno);

                            // Launch main activity
                            AlertDialog alertDialog = new AlertDialog.Builder(id_validation.this).create();
                            alertDialog.setTitle("Welcome");
                            alertDialog.setMessage(name + "\n " + sid);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(id_validation.this, studentMainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    });
                            alertDialog.show();
                            alertDialog.getWindow().getAttributes();
                            TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                            textView.setTextSize(20);
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid", sida);



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
