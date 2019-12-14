package com.example.nandan.myapplication.faculty;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.setup.AppConfig;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.Integer.parseInt;

/**
 * Created by nandan on 21/5/16.
 */
class comment
{
    String sid;
    String dropdown;
    String comment;
    float rating;


    comment(String sid,String dropdown,String comment,float rating){
        this.sid = sid;
        this.rating = rating;
        this.dropdown = dropdown;
        this.comment = comment;

    }
}
public class commentdisplay extends AppCompatActivity {
    float noct;
    int hcot1;
    int[] a =new int[6];
    private List<comment> attends = new ArrayList<>();
    private void initializeData(String sid,String dropdown,String comment,float rating){
        attends.add(new comment(sid,dropdown,comment,rating));
        Log.d("intialize1",""+sid+" "+dropdown+" "+comment);
    }
    SQLiteHandler db;
    String comp,test="finish";
    private commentAdapter adapter;
    CardView cv;
    private HorizontalBarChart chart;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        final String subcode = intent.getStringExtra("subcode");
//        Log.d("subcode ",subcode);
        getSupportActionBar().setTitle(subcode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String,String> user1 = db.getUserDetails();
        getcomment atc = new getcomment();
        try {
            comp =atc.execute(user1.get("fid"),subcode).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(test.equals(comp)) {
            cv = (CardView) findViewById(R.id.cv);
            TextView tx1 = (TextView) findViewById(R.id.ratevalue);
            tx1.setText(new DecimalFormat("#.#").format(noct));
            TextView tx2 = (TextView) findViewById(R.id.hcot1);
            tx2.setText(""+hcot1);
            RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar12);
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.rgb(255,215,0), PorterDuff.Mode.SRC_ATOP);
            ratingBar.setRating(noct);
            chart = (HorizontalBarChart) findViewById(R.id.chart1);
            BarData data = new BarData(getXAxisValues(), getDataSet());
            data.setValueFormatter(new MyValueFormatter());
            chart.setData(data);
            chart.getXAxis().setEnabled(false);
            YAxis leftAxis = chart.getAxisLeft();
            chart.getAxisRight().setEnabled(false);
            leftAxis.setEnabled(false);
            chart.animateXY(2000, 2000);
            chart.invalidate();
            chart.setClickable(false);
            chart.setDescription(""); // Hide the description
            chart.getLegend().setEnabled(false);
            chart.setDoubleTapToZoomEnabled(false);
            chart.setPinchZoom(false);
            leftAxis.setDrawLabels(true);
            Log.d("enterd successfully"," "+test+" "+comp);
            RecyclerView rv=(RecyclerView)findViewById(R.id.rv3);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            rv.setLayoutManager(llm);
           commentAdapter adpt=new commentAdapter(attends);
            rv.setAdapter(adpt);
        }
        if(comp.equals("error"))
            Toast.makeText(getApplicationContext(),
                    "No feedback", Toast.LENGTH_SHORT).show();

    }
    private class getcomment extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... X){
            String fid = X[0];
            String subcode2 = X[1];
            Log.d("request!", "starting");

            HttpClient httpclient = new DefaultHttpClient();
            String jsonResult =null;
            HttpPost httppost = new HttpPost(AppConfig.URL_COMMENT);
            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>(2);
                params1.add(new BasicNameValuePair("Fid", fid));
                params1.add(new BasicNameValuePair("subject",subcode2));
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
                    int i = jObj.getInt("count");
                    Log.d("i",""+i);
                    int j=0;
                    while(i!=j) {
                        noct = (float) jObj.getDouble("noofstars");
                        hcot1 = jObj.getInt("hcot");
                        a[0] = jObj.getInt("a1");
                        a[1] = jObj.getInt("a2");
                        a[2] = jObj.getInt("a3");
                        a[3] = jObj.getInt("a4");
                        a[4] = jObj.getInt("a5");
                        Log.d("values","entered while 1");
                        initializeData(jObj.getString("sid"+j), jObj.getString("dropbox"+j),jObj.getString("comment"+j),(float)jObj.getDouble("rating"+j));
                        j++;
                    }
                    Log.d("i",""+j);
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
            return "finish";
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent productIntent = new Intent(commentdisplay.this, MainActivity.class);
            //Start Product Activity
            startActivity(productIntent);
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private ArrayList getDataSet() {
        ArrayList dataSets = null;

        ArrayList valueSet1 = new ArrayList<>();
        for(int i=0;i<5;i++) {
            BarEntry v1e1 = new BarEntry(a[i],i);
            valueSet1.add(v1e1);
        }
        BarDataSet barDataSet1 = new BarDataSet(valueSet1,"Asset");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }
    private ArrayList getXAxisValues() {
        ArrayList xAxis = new ArrayList<>();
        xAxis.add("1");
        xAxis.add("2");
        xAxis.add("3");
        xAxis.add("4");
        xAxis.add("5");
        return xAxis;
    }
    public class MyValueFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return Math.round(value) + ""; // e.g. append a dollar-sign
        }
    }
}
