package com.example.nandan.myapplication.student.GUI.Action_About;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.student.GUI.studentMainActivity;
import com.example.nandan.myapplication.student.setup.SQLiteHandler;
import com.example.nandan.myapplication.student.setup.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class person
{
    String sid;
    String name;
    String sem;
    String sec;
    String dept;
    String email;
    String mobno;
    person(String sid,String name,String sem,String sec,String dept,String email,String mobno){
        this.sid=sid;
        this.name=name;
        this.sem=sem;
        this.sec=sec;
        this.dept=dept;
        this.email=email;
        this.mobno=mobno;
    }
}

public class Scroll_activity extends AppCompatActivity {
    private SQLiteHandler db;
    private SessionManager session;
    private List<person> persons;
    private void initializeData(String sid,String name,String sem,String sec,String dept,String email,String mobno){
        persons = new ArrayList<>();
        persons.add(new person(sid,name,sem,sec,dept,email,mobno));
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
        HashMap<String, String> user1 = db.getUserDetails1();

        String sid = user1.get("sid");
        String name = user1.get("name");
        String sem = user1.get("sem");
        String sec = user1.get("sec");
        String dept = user1.get("dept");
        String email = user1.get("email");
        String mobno = user1
                .get("mobno");
        TextView tx1= (TextView) findViewById(R.id.tx1);
        tx1.setText(name);
        TextView tx2= (TextView) findViewById(R.id.tx2);
        tx2.setText(sid);
        initializeData(sid, name, sem, sec, dept, email, mobno);
        RVAdapter adpt=new RVAdapter(persons);
        rv.setAdapter(adpt);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            Intent productIntent = new Intent(Scroll_activity.this,studentMainActivity.class);
            //Start Product Activity
            startActivity(productIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
