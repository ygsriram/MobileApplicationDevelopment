package com.example.nandan.myapplication.faculty;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.faculty.Action_About.timetable;

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by nandan on 14/5/16.
 */
public class AppraisalAdapter extends RecyclerView.Adapter<AppraisalAdapter.PersonViewHolder>{
    List<appraisal> attends;
    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView subcode,noct,head;
        RatingBar ratingBar;
        SQLiteHandler db;
        PersonViewHolder(View itemView){
            super(itemView);
            db = new SQLiteHandler(itemView.getContext());
            final HashMap<String, String> user = db.getSubject();
            final int i = Integer.parseInt(user.get("entry"));

            cv=(CardView)itemView.findViewById(R.id.cv);
            subcode=(TextView)itemView.findViewById(R.id.subcode);
            noct=(TextView)itemView.findViewById(R.id.noct1);
            head = (TextView) itemView.findViewById(R.id.noct);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.rgb(255,215,0), PorterDuff.Mode.SRC_ATOP);
            String sub_name=subcode.getText().toString();
            final Float rating = ratingBar.getRating();
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sub_name=subcode.getText().toString();
                    Float rating = ratingBar.getRating();
                    String subcode=null;
                    int j=0;
                    while(i!=j) {
                        if(sub_name.equals(user.get("subname"+j))) {
                            Log.d("data", "" + user.get("subcode" + j));
                            subcode = user.get("subcode" + j);
                        }j++;
                        if(rating!=0.0) {
                            Intent intent = new Intent(cv.getContext(), commentdisplay.class);
                            intent.putExtra("subcode", subcode);
                            cv.getContext().startActivity(intent);
                        }
                        else
                        {
                            new SweetAlertDialog(cv.getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("No feedback received")
                                    .setContentText(" ")
                                    .setConfirmText("Okay")
                                    .show();
                        }
                    }
                }
            });
        }
    }
    AppraisalAdapter(List<appraisal> attends){
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
        holder.noct.setText(attends.get(i).date);
        holder.head.setText("Head Count " + attends.get(i).hcot);
        holder.ratingBar.setRating(attends.get(i).rating);

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView rcv)

    {
        super.onAttachedToRecyclerView(rcv);
    }
}