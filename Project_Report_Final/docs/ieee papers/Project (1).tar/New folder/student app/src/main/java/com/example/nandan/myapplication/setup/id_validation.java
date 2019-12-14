package com.example.nandan.myapplication.setup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
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


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by nandan on 16/3/16.
 */
public class id_validation extends AppCompatActivity {
    private Button btnLogin;
    private EditText sid;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    String comp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sid_validate);

        sid = (EditText) findViewById(R.id.fid);
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


        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                final String sida = sid.getText().toString().trim();
                String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Check for empty data in the form
                if (!sida.isEmpty()) {
                    getcomment atc = new getcomment();
                    try {
                        comp = atc.execute(sida,id).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(comp.equals("finish")){
                        new SweetAlertDialog(id_validation.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Password Sent")
                                .setContentText("Password is sent to your registered ID. \n Password not received? \nContact the admin.")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(id_validation.this, Login.class);
                                        intent.putExtra("sid", sida);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                        finish();
                                    }
                                })
                                .show();

                    }
                    else {
                        new SweetAlertDialog(id_validation.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Your are logged in!")
                                .show();

                    }
                } else {
                    new SweetAlertDialog(id_validation.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Enter your id!")
                            .show();
                }
            }

        });

        // Link to Register Screen


    }

    private class getcomment extends AsyncTask<String, Void, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(id_validation.this);
            pdia.setMessage("Loading...");
            pdia.setIndeterminate(true);
            pdia.show();
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pdia.dismiss();
        }
        @Override
        protected String doInBackground(String... X){
            String fid = X[0];
            String id = X[1];
            Log.d("request!", "starting");

            HttpClient httpclient = new DefaultHttpClient();
            String jsonResult =null;
            HttpPost httppost = new HttpPost(AppConfig.URL_SIDVALIDATE);
            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>(2);
                params1.add(new BasicNameValuePair("sid", fid));
                params1.add(new BasicNameValuePair("deviceid",id));
                httppost.setEntity(new UrlEncodedFormEntity(params1));
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
                Log.d("jsonResult",jsonResult);
            }
            catch (ClientProtocolException e) {
                Log.d("ClientProtocolExecption","error");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("ClientProtocolExecption","error");
                e.printStackTrace();
            }
            try {
                JSONObject jObj = new JSONObject(jsonResult);
                boolean error = jObj.getBoolean("error");
                Log.d("json",""+jObj);
                // Check for error node in json
                if (!error) {
                    return "finish";
                } else {
                    // Error in login. Get the error message
                    return "error";
                    /*String errorMsg = jObj.getString("sid0");
                    Log.d("Toast ","Inside toast");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_SHORT).show();*/
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return null;
        }
        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }
            catch (IOException e) {
                // e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }
    }
}
