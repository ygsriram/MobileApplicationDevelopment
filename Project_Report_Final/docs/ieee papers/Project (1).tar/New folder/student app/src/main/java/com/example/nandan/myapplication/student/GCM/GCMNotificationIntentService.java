package com.example.nandan.myapplication.student.GCM;

/**
 * Created by pc on 22/3/16.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.nandan.myapplication.R;
import com.example.nandan.myapplication.setup.AppConfig;
import com.example.nandan.myapplication.student.GUI.Rating;
import com.example.nandan.myapplication.student.setup.SQLiteHandler;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;

    NotificationCompat.Builder builder;
    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: ");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        );
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                for (int i = 0; i < 3; i++) {
                    //          Log.i(TAG,"Working... " + (i + 1) + "/5 @ "+ SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }

                }
                //Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());


                //Log.i(TAG, "Received: " + extras.toString());
                String fid = extras.get(AppConfig.FID).toString();
                String subcode = extras.get(AppConfig.SUBCODE).toString();
                String subname = extras.get("subname").toString();
                String fname = extras.get("fname").toString();
                sendNotification("Provide your feedback ");
                SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                db.addtempvalues(subcode,fid,subname,fname);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    //public String fid = extras.get(AppConfig.FID).toString();
    private void sendNotification(String msg) {
        //Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this,Rating.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.mipmap.feedback_icon)
                .setContentTitle("Feedback")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(true);
        AudioManager am = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);

    /* Even if the mode is set to "Sound & Vibration" in the phone,
     * the status code that getRingerMode() returns is RINGER_MODE_NORMAL.
     */
        switch (am.getRingerMode())
        {
            case AudioManager.RINGER_MODE_VIBRATE:
                mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                break;
            default:
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        //Log.d(TAG, "Notification sent successfully.");
    }
}


