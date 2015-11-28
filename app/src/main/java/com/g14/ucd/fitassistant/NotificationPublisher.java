package com.g14.ucd.fitassistant;

import android.app.IntentService;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by rodrigofarias on 11/23/15.
 */

/**
 * Notification Broadcast Receiver
 */
public class NotificationPublisher extends BroadcastReceiver{


    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    private Intent mServiceIntent;

    public void onReceive(Context context, Intent intent) {
        NotificationFitAssistant nFitAssistant = new NotificationFitAssistant();
        nFitAssistant.onHandleIntent(intent);
        Log.d(CommonConstants.DEBUG_TAG, intent.getAction());
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);


    }
}