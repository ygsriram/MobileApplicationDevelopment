package com.example.nandan.myapplication.student.GUI;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.nandan.myapplication.R;

import java.util.List;

/**
 * Created by nandan on 28/4/16.
 */
public class AttendAdapter extends RecyclerView.Adapter<AttendAdapter.PersonViewHolder>{
    List<attend> attends;
    public static class PersonViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView subcode,subname,noct,nota;
        PersonViewHolder(View itemView){
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.cv);
            subcode=(TextView)itemView.findViewById(R.id.subcode);
            subname=(TextView)itemView.findViewById(R.id.subname);
            noct=(TextView)itemView.findViewById(R.id.noct);
            nota=(TextView)itemView.findViewById(R.id.noat);
        }
    }
    AttendAdapter(List<attend> attends){
        this.attends=attends;
    }
    @Override
    public int getItemCount()
    {
        Log.d("Item Count",""+attends.size());
        return attends.size();
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tab_view2,viewGroup,false);
        PersonViewHolder pvh=new PersonViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(PersonViewHolder holder, int i) {
        holder.subcode.setText(attends.get(i).subcode);
        holder.subname.setText(attends.get(i).subname);
        holder.noct.setText(attends.get(i).noct);
        holder.nota.setText(attends.get(i).noat);
        //////holder.tv6.setText("Number: " + persons.get(i).mobno);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView rcv)

    {
        super.onAttachedToRecyclerView(rcv);
    }

}
