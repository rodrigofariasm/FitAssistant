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
public class NotificationFitAssistant extends IntentService{
    private NotificationManager mNotificationManager;
    private String mMessage;
    private int mMillis;
    NotificationCompat.Builder builder;
 public NotificationFitAssistant() {

 // The super call is required. The background thread that IntentService
 // starts is labeled with the string argument you pass.
 super("com.g14.ucd.fitassistant");
 }

    @Override
    protected void onHandleIntent(Intent intent) {
        // The reminder message the user set.
        mMessage = intent.getStringExtra(CommonConstants.EXTRA_MESSAGE);
        // The timer duration the user set. The default is 10 seconds.
        mMillis = intent.getIntExtra(CommonConstants.EXTRA_TIMER,
                CommonConstants.DEFAULT_TIMER_DURATION);
        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        String action = intent.getAction();
        // This section handles the 3 possible actions:
        // ping, snooze, and dismiss.
        if(action.equals(CommonConstants.ACTION_PERFORMED)) {
            issueNotification(intent, mMessage);
        } else if (action.equals(CommonConstants.ACTION_UNPERFORMED)) {
            nm.cancel(CommonConstants.NOTIFICATION_ID);
            //Log.d(CommonConstants.DEBUG_TAG, getString(R.string.snoozing));
            // Sets a snooze-specific "done snoozing" message.
            issueNotification(intent,"done unperformed");

        } else if (action.equals(CommonConstants.ACTION_PING)) {
            nm.cancel(CommonConstants.NOTIFICATION_ID);
        }
    }

    private void issueNotification(Intent intent, String msg) {
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        // Sets up the Snooze and Dismiss action buttons that will appear in the
        // expanded view of the notification.
        Intent dismissIntent = new Intent(this, MainActivity.class);
        dismissIntent.setAction(CommonConstants.ACTION_PERFORMED);
        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(this, MainActivity.class);
        snoozeIntent.setAction(CommonConstants.ACTION_UNPERFORMED);
        PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);

        // Constructs the Builder object.
        builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_local_dining_white_24dp)
                        .setContentTitle("Notification!!!")
                        .setContentText("It is time for breakfast")
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                /*
                 * Sets the big view "big text" style and supplies the
                 * text (the user's reminder message) that will be displayed
                 * in the detail area of the expanded notification.
                 * These calls are ignored by the support library for
                 * pre-4.1 devices.
                 */
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .addAction(R.drawable.ic_local_dining_white_24dp,
                                getString(R.string.date_of_birth), piDismiss)
                        .addAction(R.drawable.ic_fitness_center_black_24dp,
                                getString(R.string.facebook_app_id), piSnooze);

        /*
         * Clicking the notification itself displays ResultActivity, which provides
         * UI for snoozing or dismissing the notification.
         * This is available through either the normal view or big view.
         */
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(CommonConstants.EXTRA_MESSAGE, msg);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);

    }

    private void issueNotification(NotificationCompat.Builder builder) {
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // Including the notification ID allows you to update the notification later on.
        mNotificationManager.notify(CommonConstants.NOTIFICATION_ID, builder.build());
    }



}
