package com.g14.ucd.fitassistant.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by rodrigofarias on 11/22/15.
 */
public class NotificationManager extends IntentService{
    private NotificationManager mNotificationManager;
    private String mMessage;
    private int mMillis;
    NotificationCompat.Builder builder;

}
