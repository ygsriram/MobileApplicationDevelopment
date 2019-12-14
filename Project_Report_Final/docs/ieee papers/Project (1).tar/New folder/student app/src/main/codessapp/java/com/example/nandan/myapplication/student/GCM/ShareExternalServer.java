package com.example.nandan.myapplication.student.GCM;

/**
 * Created by pc on 22/3/16.
 */

import android.content.Context;
import android.util.Log;

import com.example.nandan.myapplication.setup.AppConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.nandan.myapplication.student.GCM.JsonParser.makeHttpRequest;


public class ShareExternalServer {
//-------------------------------------------------sid-------------------
    public String shareRegIdWithAppServer(final Context context,
                                          final String regId,final String Sid) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("regID", regId));
        params.add(new BasicNameValuePair("Sid", Sid));

        Log.d("request!", "starting");
        // getting product details by making HTTP request
        JSONObject json = makeHttpRequest(
                AppConfig.APP_SERVER_URL, "POST", params);

        // check your log for json response
       // Log.d("Login attempt", json.toString());

        // json success tag
        String result ="success";
        return result;
    }
}