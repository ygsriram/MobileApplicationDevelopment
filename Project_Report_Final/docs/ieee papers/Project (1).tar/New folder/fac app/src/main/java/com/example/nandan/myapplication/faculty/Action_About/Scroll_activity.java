package com.example.nandan.myapplication.faculty.Action_About;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.faculty.MainActivity;
import com.example.nandan.myapplication.faculty.SQLiteHandler;
import com.example.nandan.myapplication.faculty.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class person
{
    String fid;
    String name;
    String dept;
    String email;
    String mobno;
    person(String fid,String name,String dept,String email,String mobno){
        this.fid=fid;
        this.name=name;
        this.dept=dept;
        this.email=email;
        this.mobno=mobno;
    }
}

public class Scroll_activity extends AppCompatActivity {
    private SQLiteHandler db;
    private SessionManager session;
    private List<person> persons;
    private void initializeData(String fid,String name,String dept,String email,String mobno){
        persons = new ArrayList<>();
        persons.add(new person(fid,name,dept,email,mobno));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_l);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView rv=(RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        HashMap<String, String> user1 = db.getSubject();
        //HashMap<String, String> user1 = db.getSubject();

        String fid = user.get("fid");
        String name = user.get("name");
        String dept = user.get("dept");
        String email = user.get("email");
        String mobno = user
                .get("mobno");
        TextView tx1= (TextView) findViewById(R.id.tx1);
        tx1.setText(name);
        TextView tx2= (TextView) findViewById(R.id.tx2);
        tx2.setText(fid);
        initializeData(fid, name, dept, email, mobno);
        RVAdapter adpt=new RVAdapter(persons);
        rv.setAdapter(adpt);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==android.R.id.home){
            Intent productIntent = new Intent(Scroll_activity.this,MainActivity.class);
            //Start Product Activity
            startActivity(productIntent);
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            finish();
        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
