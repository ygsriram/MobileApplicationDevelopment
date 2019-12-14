package com.example.nandan.myapplication.student.GCM;

import android.util.Log;

import com.example.nandan.myapplication.setup.AppConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.nandan.myapplication.student.GCM.JsonParser.makeHttpRequest;

/**
 * Created by pc on 15/4/16.
 */

public class ShareRating {

    //------------------------------------------------sid-------------------
    public String shareRegIdWithAppServer1(final String Rating) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("Rating", Rating));

        Log.d("request!", "starting");
        // getting product details by making HTTP request
        JSONObject json = makeHttpRequest(
                AppConfig.APP_Rating_URL, "POST", params);

        // check your log for json response
        // Log.d("Login attempt", json.toString());

        // json success tag
        String result ="success";
        return result;
    }
}
