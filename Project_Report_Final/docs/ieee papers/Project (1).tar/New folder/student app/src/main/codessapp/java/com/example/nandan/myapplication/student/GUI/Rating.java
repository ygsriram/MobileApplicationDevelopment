package com.example.nandan.myapplication.student.GUI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nandan.myapplication.R;


/**
 * Created by pc on 2/4/16.
 */
public class Rating extends Activity {
    Context context=this;
    ShareRating appUtil;
    String Rating;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appUtil = new ShareRating();

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("Appraisal");

        // set the custom dialog components - text, image and button
        final TextView txtRatingValue;
        RatingBar ratingBar;

        txtRatingValue = (TextView) dialog.findViewById(R.id.txtRatingValue);

        ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));
                Rating=String.valueOf(rating);


            }
        });




        Button dialogButton = (Button) dialog.findViewById(R.id.sub);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (Rating.equals(null)) {
                    Toast.makeText(Rating.this,Rating, Toast.LENGTH_SHORT).show();
                }
                else {
                    registerInBackground2();
                }
            }

        });

        dialog.show();



    }
    private void registerInBackground2(){
        registerInBackground1();
    }
    private void registerInBackground1() {
        new AsyncTask<Void,Void,String>() {
            protected String doInBackground(Void... params) {
                //----------------------------sid
                return appUtil.shareRegIdWithAppServer1(Rating);
            }


            protected void onPostExecute(String result) {
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }

        }.execute(null, null, null);

    }

}
