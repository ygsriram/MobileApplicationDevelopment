package com.example.nandan.myapplication.student.GUI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.student.GCM.ShareExternalServer;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nandan on 16/3/16.
 */
public class internals extends android.support.v4.app.Fragment {
    public internals(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_view,container,false);
        RecyclerView rv=(RecyclerView)rootView.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        return rootView;
    }
}