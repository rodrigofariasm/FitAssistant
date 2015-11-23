package com.g14.ucd.fitassistant.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.g14.ucd.fitassistant.MainActivity;
import com.g14.ucd.fitassistant.R;

/**
 * Created by rodrigofarias on 11/22/15.
 */
public class NotificationFitAssistant {
    private NotificationManager mNotificationManager;
    private String mMessage;
    private int mMillis;
    NotificationCompat.Builder builder;
     public NotificationFitAssistant() {

     }

    private void issueNotification(Intent intent, String msg) {

    }

    private void issueNotification(NotificationCompat.Builder builder) {

    }



}
