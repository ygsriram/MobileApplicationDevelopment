package com.example.nandan.myapplication.faculty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.Integer.parseInt;

/**
 * Created by vigne on 20-04-2016.
 */
public class SendNotification extends android.support.v4.app.Fragment {
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    String subcode;
    String sec;
    //String[] subject = new String[];
    String room;
    int j=0;
    Spinner s0,s1,s2;
    private SQLiteHandler db;


//    internals context=this;
    AsyncTask shareRegidTask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_view,container,false);

        final EditText ed1 = (EditText)rootView.findViewById(R.id.editText);
        final EditText ed2 = (EditText)rootView.findViewById(R.id.editText2);
        final EditText ed3 = (EditText)rootView.findViewById(R.id.editText3);

        db = new SQLiteHandler(getActivity());

        final HashMap<String, String> user = db.getSubject();
        HashMap<String, String> user1 = db.getUserDetails();
        final String fid = user1.get("fid");
        final int i = parseInt(user.get("entry"));


        Button submit=(Button)rootView.findViewById(R.id.sub);
//String subcode1 = ed1.getText().toString();
        if (submit != null) {
            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                   // Log.d("Main",""+subcode+sec+room);
                    int j=0;
                     int flag = 1;
                    while(i!=j){
                        if(user.get("subcode"+j).equals(ed1.getText().toString()))
                            flag=0;
                         j++;
                    }
                    if(flag==0) {
                        sendnotification(ed1.getText().toString(), ed2.getText().toString(), ed3.getText().toString(), fid);

                    }else
                        Toast.makeText(getContext(),"Enter proper subject code",Toast.LENGTH_SHORT).show();
                }
            });
        }
        Button stop = (Button) rootView.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("fid and subcode","" + ed1.getText().toString() );
                sendstopnotice(fid,ed1.getText().toString());
            }
        });

        return rootView;
    }


       public void sendnotification(final String subcode, final String sec, final String roomno, final String fid){
        String tag_string_req = "req_feedback";
        Log.d("Hello World","Hello world" + subcode + sec + roomno+fid);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.APP_SERVER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("login response", "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("err_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getContext(),
                                "Not able to send Apprisal request", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("login error", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            int count=0;
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                Log.d("hello world","sdkjfhsdkfbsk "+subcode+sec+roomno+fid);
                params.put("subcode", subcode);
                params.put("sec", sec);
                params.put("room", roomno);
                params.put("fid",fid);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void sendstopnotice(final String fid, final String subcode){
        String tag_string_req = "req_feedback";
        Log.d("Hello World","Hello world" + subcode + fid);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STOP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("login response", "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    Boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        if (jObj.getInt("add") == 1) {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(jObj.getString("error_msg"))
                                    .setContentText(" ")
                                    .setCancelText("No,Reject!")
                                    .setConfirmText("Yes,Accept!")
                                    .showCancelButton(true)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.setTitleText("Confirm")
                                                    .setContentText("Are you sure to reject the collected appraisals?")
                                                    .setConfirmText("OK")
                                                    .showCancelButton(false)
                                                    .setCancelClickListener(null)
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(final SweetAlertDialog sDialog) {
                                                            sDialog.setTitleText("Deleted!")
                                                                    .setContentText("All the appraisal are deleted")
                                                                    .setConfirmText("OK")
                                                                    .showCancelButton(false)
                                                                    .setCancelClickListener(null)
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                            headcountcancel(fid, subcode);
                                                                            sDialog.dismiss();
                                                                        }
                                                                    })
                                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                        }
                                                    })
                                                    .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            // reuse previous dialog instance, keep widget user state, reset them if you need
                                            sDialog.setTitleText("Success!")
                                                    .setContentText("The feedback is recorded :)")
                                                    .setConfirmText("OK")
                                                    .showCancelButton(false)
                                                    .setCancelClickListener(null)
                                                    .setConfirmClickListener(null)
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        }

                                    })
                                    .show();
                        }
                        else if(jObj.getInt("add")==0){
                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(jObj.getString("error_msg"))
                                    .setContentText(" ")
                                    .setConfirmText("Ok")
                                    .show();
                        }
                    }
                    else{
                        //Toast.makeText(getContext(),
                                //"Not able to send Apprisal request", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("login error", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            int count=0;
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                Log.d("hello world","sdkjfhsdkfbsk "+subcode+fid);
                params.put("fid", fid);
                params.put("subcode", subcode);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void headcountcancel(final String fid, final String subcode){
        String tag_string_req = "req_feedback";
        Log.d("Hello World","Hello world" + subcode + fid);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REMOVE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("login response", "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                    }
                    else{
                        //Toast.makeText(getContext(),
                        //"Not able to send Apprisal request", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("login error", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            int count=0;
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                Log.d("hello world","sdkjfhsdkfbsk "+subcode+fid);
                params.put("fid", fid);
                params.put("subcode", subcode);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
