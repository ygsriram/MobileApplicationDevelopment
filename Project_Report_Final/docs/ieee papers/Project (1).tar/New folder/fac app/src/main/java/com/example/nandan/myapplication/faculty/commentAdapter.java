package com.example.nandan.myapplication.faculty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nandan.myapplication.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nandan on 21/5/16.
 */
public class commentAdapter extends RecyclerView.Adapter<commentAdapter.PersonViewHolder>{
    List<comment> attends;
    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView sid,dropdown,comment;
        RatingBar ratingBar;
        SQLiteHandler db;
        PersonViewHolder(View itemView){
            super(itemView);
            db = new SQLiteHandler(itemView.getContext());
            final HashMap<String, String> user = db.getSubject();
            final int i = Integer.parseInt(user.get("entry"));

            cv=(CardView)itemView.findViewById(R.id.cv);
            sid=(TextView)itemView.findViewById(R.id.sid);
            dropdown=(TextView)itemView.findViewById(R.id.dropdown);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar12);
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.rgb(255,215,0), PorterDuff.Mode.SRC_ATOP);

        }
    }
    commentAdapter(List<comment> attends){
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
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row,viewGroup,false);
        PersonViewHolder pvh=new PersonViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(PersonViewHolder holder, int i) {
        holder.sid.setText(attends.get(i).dropdown);
        holder.dropdown.setText(attends.get(i).comment);
        holder.ratingBar.setRating(attends.get(i).rating);

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView rcv)

    {
        super.onAttachedToRecyclerView(rcv);
    }
}