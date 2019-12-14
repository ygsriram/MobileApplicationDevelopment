package com.example.nandan.myapplication.faculty;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.setup.AppConfig;

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
import java.util.concurrent.ExecutionException;

import static java.lang.Integer.parseInt;

/**
 * Created by vigne on 20-04-2016.
 */
class appraisal
{
    String subcode;
    Float rating;
    String date;
    int hcot;
    appraisal(String subcode,Float rating,String date,int hcot){
        this.subcode = subcode;
        this.rating = rating;
        this.date = date;
        this.hcot = hcot;
    }
}
public class AppraisalResult extends android.support.v4.app.Fragment  {
    private List<appraisal> attends = new ArrayList<>();
    private void initializeData(String subcode,Float rating,String date,int hcot){
        attends.add(new appraisal(subcode,rating,date,hcot));
        Log.d("intialize",""+subcode+" "+rating+" "+date);
    }
    public AppraisalResult() {

    }
    SQLiteHandler db;
    HashMap<String,String> user2;
    String comp,test;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteHandler(getContext());
        Log.d("create attendance","check 1");
        HashMap<String,String> user1 = db.getUserDetails();
        Log.d("database ","" + user1.size());
        user2 = db.getSubject();
        int i=0;
        int j=parseInt(user2.get("entry"));
        while(i!=j)
        {
            getappraisal atc = new getappraisal();
            {
                try {
                    comp=atc.execute(user1.get("fid"), user2.get("subcode"+i),""+i).get();
                    Log.d("comp",comp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }

        test = "finish"+(i-1);

    }
    private SwipeRefreshLayout swipecontainer;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycle,container,false);
        if(comp.equals(test)) {
            Log.d("run ui", comp + " " + attends.size());
            RecyclerView rv=(RecyclerView)rootView.findViewById(R.id.rv);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rv.setLayoutManager(llm);
            AppraisalAdapter adpt=new AppraisalAdapter(attends);
            rv.setAdapter(adpt);
        }
        /*swipecontainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipecontainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshdata();
            }
        });*/


        return rootView;
    }
    private class getappraisal extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... X){
            String sida = X[0];
            String subcode2 = X[1];
            String m = X[2];
            Log.d("request!", "starting");
            HttpClient httpclient = new DefaultHttpClient();
            String jsonResult =null;
            HttpPost httppost = new HttpPost(AppConfig.URL_APPRAISAL);
            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>(2);
                params1.add(new BasicNameValuePair("fid", sida));
                params1.add(new BasicNameValuePair("subcode",subcode2));
                httppost.setEntity(new UrlEncodedFormEntity(params1));
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
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
                    float noat = (float) jObj.getDouble("noofstars");
                    String subcode = jObj.getString("subcode");
                    String time = jObj.getString("time");
                    int hcot = jObj.getInt("hcot");
                    Log.d("try", "" + user2.get("subcode0") + " " + subcode2);

                    initializeData(subcode,noat,time,hcot );
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Log.d("Toast ","Inside toast");
                    Toast.makeText(getActivity(),
                            errorMsg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return "finish"+m;
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
                Toast.makeText(getContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }
    }
}
