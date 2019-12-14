package com.example.nandan.myapplication.student.GUI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.setup.AppConfig;
import com.example.nandan.myapplication.student.setup.SQLiteHandler;
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
 * Created by nandan on 16/3/16.
 */
class attend
{
    String subcode;
    String subname;
    String noct;
    String noat;
    String date;

    attend(String subcode,String subname,String noct,String noat,String date){
        this.subcode = subcode;
        this.subname = subname;
        this.noct = noct;
        this.noat = noat;
        this.date = date;
    }
}
public class attendance extends android.support.v4.app.Fragment{
    private List<attend> attends = new ArrayList<>();
    private void initializeData(String subcode,String subname,String noct,String noat,String date){
        attends.add(new attend(subcode,subname,noct,noat,date));
        Log.d("intialize",""+subcode+" "+subname+" "+noct+" "+noat+" "+date);
    }
    public attendance() {

    }
    SQLiteHandler db;
    HashMap<String,String>user2;
    String comp,test;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteHandler(getContext());
        Log.d("create attendance","check 1");
        HashMap<String,String>user1 = db.getUserDetails1();
        Log.d("database ","" + user1.size());
        user2 = db.getSubjectDetails1();
        int i=0;
        int j=parseInt(user2.get("entry"));
while(i!=j)
        {
            getattendancestatus atc = new getattendancestatus();
            {
                try {
                    comp=atc.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,user1.get("sid"), user2.get("subcode"+i), user1.get("sec"),""+i).get();
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
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycle,container,false);
        if(comp.equals(test)) {
            Log.d("run ui", comp + " " + attends.size());
            RecyclerView rv=(RecyclerView)rootView.findViewById(R.id.rv);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rv.setLayoutManager(llm);
            AttendAdapter adpt=new AttendAdapter(attends);
            rv.setAdapter(adpt);
        }
        return rootView;
    }
    private class getattendancestatus extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... X){
            String sida = X[0];
            String subcode2 = X[1];
            String sec1 = X[2];
            String m = X[3];
            Log.d("request!", "starting");
            HttpClient httpclient = new DefaultHttpClient();
            String jsonResult =null;
            HttpPost httppost = new HttpPost(AppConfig.URL_ATTENDANCE);
            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>(2);
                params1.add(new BasicNameValuePair("Sid", sida));
                params1.add(new BasicNameValuePair("subcode",subcode2));
                params1.add(new BasicNameValuePair("sec",sec1));
                httppost.setEntity(new UrlEncodedFormEntity(params1));
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jObj = new JSONObject(jsonResult);
                boolean error = jObj.getBoolean("error");
                Log.d("json",""+jObj);
                // Check for error node in json
                if (!error) {
                    int noat = jObj.getInt("no_att");
                    int noct = jObj.getInt("noct");
                    String subcode = jObj.getString("subcode");
                    String time = jObj.getString("time");
                    String subname = null;
                    Log.d("try", "" + user2.get("subcode0") + " " + subcode2);
                    int i = parseInt(user2.get("entry"));
                    int j = 0;
                    while (i != j) {
                        if (user2.get("subcode" + j).equals(subcode2))
                        { subname = user2.get("subject" + j);break;
                        }
                        Log.d("while back while","" + user2.get("subcode" + j));
                        j++;
                    }
                    initializeData(subcode, subname, "" + noct, "" + noat, time);
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getActivity(),
                            errorMsg, Toast.LENGTH_LONG).show();
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