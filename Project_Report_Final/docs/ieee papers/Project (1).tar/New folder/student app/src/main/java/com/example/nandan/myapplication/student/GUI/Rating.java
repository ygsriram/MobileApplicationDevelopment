package com.example.nandan.myapplication.student.GUI;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.setup.AppConfig;
import com.example.nandan.myapplication.setup.AppController;
import com.example.nandan.myapplication.student.setup.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by pc on 2/4/16.
 */
public class Rating extends Activity implements AdapterView.OnItemSelectedListener {
    Context context = this;
    private LocationManager locationManager;
    String Rating;
    double lat,lon;
    String feedback;

    ProgressDialog pdialog;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdialog = new ProgressDialog(this);
        Intent rate = getIntent();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user1 = db.gettempDetails1();
        final String fid = user1.get("fid");
        final String subcode = user1.get("subcode");
        final String subname = user1.get("subname");
        final String fname = user1.get("fname");
        Log.d("input data",subname + " "+ fname + " " + user1.get("subname") + " "+ user1.get("fname"));
        db.deletetemp();
        Log.d("oncreate", "" + fid + subcode + subname + fname);
        final Dialog dialog = new Dialog(context);

        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom);
        //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        // set the custom dialog components - text, image and button
        final TextView txtRatingValue,tx1,tx2;
        final EditText et1;
        et1 = (EditText) dialog.findViewById(R.id.message_text);

        RatingBar ratingBar;
        tx1 = (TextView) dialog.findViewById(R.id.textView11);
        tx2 = (TextView) dialog.findViewById(R.id.textView12);
        tx1.setText(subname);
        tx2.setText(fname);
        txtRatingValue = (TextView) dialog.findViewById(R.id.txtRatingValue);
        ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));
                Rating = String.valueOf(rating);


            }
        });
       Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Topic understood");
        categories.add("Got a jist but need more explanation");
        categories.add("Have doubts");
        categories.add("The Presentation was not clear");
        categories.add("Topic not understood");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        Log.d("feedback2"," "+feedback);
        Button dialogButton = (Button) dialog.findViewById(R.id.sub);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (Rating.equals(null)) {
                    Toast.makeText(Rating.this, Rating, Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                    HashMap<String, String> user1 = db.getUserDetails1();
                    final String sid = user1.get("sid");
                    if (ActivityCompat.checkSelfPermission(Rating.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Rating.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        Log.d("permission", "permission not granted");
                        ActivityCompat.requestPermissions(Rating.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSION_ACCESS_COARSE_LOCATION);
                    }

                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 12000, 10, Rating.this);
                    Log.d("feedback2"," "+feedback);
                    Log.d("sending data","");
                    String comment = et1.getText().toString();
                    if(comment.equals(null))
                        comment = "The class was perfect";
                    sendrating(Rating,subcode,sid,fid,feedback,comment);
                   finish();
                }
            }

        });

        dialog.show();



    }
    public void sendrating(final String rating, final String subcode, final String sid, final String fid,final String feedback,final String comment){
        String tag_string_req = "req_attendance";
        Log.d("sendrating","Hello world" + rating + subcode + sid + fid);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.APP_RATING_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("login response", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(),jObj.getString("message"),Toast.LENGTH_LONG).show();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
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

            }
        }) {
            int count=0;
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                Log.d("hello world","sdkjfhsdkfbsk "+ sid + subcode + rating + fid);
                params.put("Sid", sid);
                params.put("Rating", rating);
                params.put("Fid", fid);
                params.put("subject", subcode);
                params.put("comment1", feedback);
                params.put("comment2", comment);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        feedback = parent.getItemAtPosition(position).toString();
        Log.d("feedback",""+feedback);
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
