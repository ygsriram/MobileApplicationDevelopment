package com.example.nandan.myapplication.student.Action_About;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.nandan.myapplication.R;

import java.util.List;

/**
 * Created by nandan on 23/2/16.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
    List<person> persons;
    public static class PersonViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
        PersonViewHolder(View itemView){
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.cv);
            tv3=(TextView)itemView.findViewById(R.id.tx3);
            tv4=(TextView)itemView.findViewById(R.id.tx4);
            tv5=(TextView)itemView.findViewById(R.id.tx5);
            tv6=(TextView)itemView.findViewById(R.id.tx6);
            tv7=(TextView)itemView.findViewById(R.id.con_det);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v){
                    cv.getContext().startActivity(new Intent(cv.getContext(), timetable.class));
                }

            });

        }
    }
    RVAdapter(List<person> persons){
        this.persons=persons;
    }
    @Override
    public int getItemCount()
    {
        return persons.size();
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_scroll_activity,viewGroup,false);

        PersonViewHolder pvh=new PersonViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(PersonViewHolder holder, int i) {
        holder.tv3.setText(persons.get(i).sem + " Sem " + persons.get(i).sec + " Sec");
        holder.tv4.setText(persons.get(i).dept);
        holder.tv7.setText("Contact Details");
        holder.tv5.setText("Email ID: " + persons.get(i).email);
        holder.tv6.setText("Number: " + persons.get(i).mobno);

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView rcv)

    {
        super.onAttachedToRecyclerView(rcv);
    }

}