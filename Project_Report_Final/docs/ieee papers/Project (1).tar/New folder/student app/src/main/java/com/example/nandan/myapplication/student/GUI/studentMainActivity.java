package com.example.nandan.myapplication.student.GUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.setup.AppConfig;
import com.example.nandan.myapplication.student.GUI.Action_About.Scroll_activity;
import com.example.nandan.myapplication.student.setup.SQLiteHandler;
import com.example.nandan.myapplication.student.GCM.ShareExternalServer;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.android.gms.internal.zzir.runOnUiThread;

public class studentMainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
	    ShareExternalServer appUtil;
    AsyncTask shareRegidTask;

    GoogleCloudMessaging gcm;
    String regId;
    final Context context = this;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    int flag=0;

    static final String TAG = "Register Activity";
    String sid;
	//-------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i;
        i=getIntent();
      //sid= i.getStringExtra("Sid");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        TextView tx1=(TextView) findViewById(R.id.tx1);
        //tx1.setText("Welcome");

//---------------------------------------------------------------------------------------------------------------------------------
        appUtil = new ShareExternalServer();
        //------------------------------------------------------------


        registergcm gcm1 = new registergcm();
        if (TextUtils.isEmpty(regId)) {
            regId = registerGCM();
            Log.d("RegisterActivity", "GCM RegId: " + regId);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Already Registered with GCM Server!",
                    Toast.LENGTH_LONG).show();
        }
        //------------------------------------------------------------------------------------------
        //sid="1AY12IS100";
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user1 = db.getUserDetails1();
        sid= user1.get("sid");
        //Log.d("sid:",sid);
        if(flag==0) {
            new AsyncTask<Void, Void, String>() {

                protected String doInBackground(Void... params) {
                    //----------------------------sid
                    String result = appUtil.shareRegIdWithAppServer(context, regId, sid);
                    return result;
                }


                protected void onPostExecute(String result) {
                    shareRegidTask = null;
                   // Toast.makeText(getApplicationContext(), result,
                            //Toast.LENGTH_LONG).show();
                }

            }.execute(null, null, null);

        }


    }
    private class registergcm extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(new internals(),"Internals");
        adapter.addFragment(new attendance(),"Attendance");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) { super(manager);}
        @Override
        public Fragment getItem(int position) { return mFragmentList.get(position);}

        @Override
        public int getCount() {return mFragmentList.size();}
        public void addFragment(Fragment fragment,String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) { return mFragmentTitleList.get(position);}

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==android.R.id.home)
        {
            finish();
        }
        if (id == R.id.action_settings) {
            Intent productIntent = new Intent(studentMainActivity.this,Scroll_activity.class);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            startActivity(productIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            //Toast.makeText(getApplicationContext(),
              //      "RegId already available. RegId: " + regId,
                //    Toast.LENGTH_LONG).show();
            flag=0;
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                studentMainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(AppConfig.GOOGLE_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                    storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
              //  Toast.makeText(getApplicationContext(),
                //        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                  //      .show();
            }
        }.execute(null, null, null);
    }



    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                studentMainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }
}
